package es.uji.bicicaswebservice.scraping;

import java.util.ArrayList;
import java.util.List;

import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import es.uji.bicicasdatamodel.BicicasPoint;
import es.uji.bicicasdatamodel.Dock;
import es.uji.bicicasdatamodel.SharingPointState;
import es.uji.bicicaswebservice.dao.BicicasDAO;
import es.uji.bicicaswebservice.messages.ErrorMessages;
import es.uji.bicicaswebservice.model.BicicasPointUtils;
import es.uji.geotec.commonutils.DateUtils;
import es.uji.geotec.commonutils.Log;
import es.uji.geotec.httputils.WebExecutor;

/**
 * Scraper of the bicicas's official page to extract BicicasPoint information
 *
 */
public class BicicasScraper extends AbstractScraper<List<BicicasPoint>> {

	private static final String TAG = BicicasScraper.class.getName();
	
	private String url;

	public BicicasScraper(String url) {
		this.url = url;
	}

	@Override
	protected List<BicicasPoint> scrape(final TagNode root) {
		
		// Matches the html table which contain the bicicas sharing point information
		final String BICICAS_TABLE_CONTAINER_XPATH = "//body/table[2]/tbody/tr[2]/td/div/table/tbody/tr/td/table[.//img]";
		// Match the tag which contains the header text
		final String BICICAS_HEADER_XPATH = "./tbody/tr[1]/td[2]";
		// Match the tag which contains the status of the bicicas sharing point
		final String BICICAS_BIKE_STATUS = "./tbody/tr[3]/td[1]";
		// Match the tags which contain the status of a dock of a sharing point
		final String BICICAS_BIKE_DOCKS = "./tbody/tr[3]/td/img";
		
		final List<BicicasPoint> updatedBicicasPoints = new ArrayList<BicicasPoint>();
		try {
			// Move to the table that contains the tables representing the bicicas's sharing point states
			final Object[] tables = root.evaluateXPath(BICICAS_TABLE_CONTAINER_XPATH);
			// For each table
			for( final Object table : tables ) {
				// Apply the xpath to find the header of the station point.
				final TagNode tableNode = (TagNode) table;
				final String header = ((TagNode) tableNode.evaluateXPath(BICICAS_HEADER_XPATH)[0]).getText().toString();
				
				//The code is from position 0 to the '.' char of the header
				final Integer code = Integer.parseInt(header.substring(0, header.indexOf('.')));
				
				// If the station is online, the header contains the following sequence "[STATION NAME] EN LÍNEA" that match ".*EN.L.NEA"
				final Boolean online = header.matches(".*EN.L.NEA");
				
				// Apply the xpath to find the available and total number of bikes. The text looks like "ESTADO - (19/28)" where 19 are the available and 28 the total number of bikes
				final String status = ((TagNode) tableNode.evaluateXPath(BICICAS_BIKE_STATUS)[0]).getText().toString();
				final Integer availableBicycles = Integer.parseInt(status.substring(status.indexOf('(') + 1, status.indexOf('/')));
				
				// Apply the xpath to find the free or occupied docks of the station
				final List<Dock> docks = new ArrayList<Dock>();
				// Actually the dock node is an img node
				final Object[] docksNode = tableNode.evaluateXPath(BICICAS_BIKE_DOCKS);
				for( int i = 0; i < docksNode.length; i++ ) {
					// If the alt attribute of the node is "No hay bicicleta" the dock is empty.
					final String altText = ((TagNode) docksNode[i]).getAttributeByName("alt");
					final Dock dock = new Dock();
					// The dock value is "BicicasPoint.EMPTY_DOCK" if it's empty, or the bike code if a bike is present
					dock.bikeCode = altText.equals("No hay bicicleta") ? Dock.EMPTY_DOCK : Integer.parseInt(altText);
					docks.add(dock);
				}
				
				// Ok, now build the bicicas point object
				final BicicasPoint updated = new BicicasPoint();
				updated.code = code;
				updated.currentState = new SharingPointState();
				updated.currentState.updated = DateUtils.nowDateES();
				updated.currentState.online = online;
				updated.currentState.bicycleAvailable = availableBicycles;
				updated.currentState.docks = docks;
				
				// Get the last status of the bicicas point
				final BicicasDAO dao = new BicicasDAO();
				dao.link();
				
				final BicicasPoint current = dao.getBicicasPointByCode(code);
				
				if( current != null && current.currentState != null ) {
					// The point already has a state
					if( BicicasPointUtils.hasDockListStatusChanged(current, updated) ) {
						// The state has changed, set the outgoig and ingoing lists
						updated.currentState.ingoing = BicicasPointUtils.getIngoingBikes(current, updated);
						updated.currentState.outgoing = BicicasPointUtils.getOutgoingBikes(current, updated);
					}
				}
				
				dao.unlink();
				
				updatedBicicasPoints.add(updated);
			}
			
		} catch (XPatherException e) {
			Log.e(TAG, e, ErrorMessages.SCRAPER_ERROR_XPATH);
			throw new RuntimeException(e);
		}
		return updatedBicicasPoints;
	}

	@Override
	protected String getWebpage() {
		return WebExecutor.syncGetRequest(url);
	}

}
