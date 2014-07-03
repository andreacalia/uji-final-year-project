package es.uji.renfewebservice.scraping;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import javax.ws.rs.core.Form;

import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import es.uji.geotec.commonutils.DateUtils;
import es.uji.geotec.commonutils.Log;
import es.uji.geotec.httputils.WebExecutor;
import es.uji.renfewebservice.model.TimeTable;
import es.uji.renfewebservice.model.Trip;

/**
 * Scraper of the renfe's official page to extract a train TimeTable
 *
 */
public class RenfeWebpageScraper extends AbstractScraper<TimeTable> {

	private static final String TAG = RenfeWebpageScraper.class.getName();
	
	private static final String BASE_URL = "http://horarios.renfe.com/cer/hjcer310.jsp";
	private static final String TABLE_ROW_XPATH = "//div[@id='contenedor']//tr";
	private static final String REGION_CODE_PARAM_NAME = "nucleo";
	private static final String I_PARAM_NAME = "i";
	private static final String CP_PARAM_NAME = "cp";
	private static final String FROM_PARAM_NAME = "o";
	private static final String TO_PARAM_NAME = "d";
	private static final String DATE_PARAM_NAME = "df";
	private static final String DEPARTURE_TIME_PARAM_NAME = "ho";
	private static final String ARRIVAL_TIME_PARAM_NAME = "hd";
	private static final String INFO_PARAM_NAME = "TXTInfo";

	private String regionCode;
	private String i;
	private String cp;
	private String departureStationCode;
	private String arrivalStationCode;
	private Calendar departureDateTime;
	private Calendar arrivalDateTime;
	private String TXTInfo;

	/**
	 * Constructor used for getting all the trains between "departureDateTime" and "arrivalDateTime"
	 * 
	 * @param departureStationCode
	 * @param arrivalStationCode
	 * @param departureDateTime
	 * @param arrivalDateTime
	 */
	public RenfeWebpageScraper(final String departureStationCode, final String arrivalStationCode, final Calendar departureDateTime, final Calendar arrivalDateTime) {
		this.regionCode = "40"; // Region code 40 means Comunidad Valenciana
		this.i = "s";
		this.cp = "NO";
		this.departureStationCode = departureStationCode;
		this.arrivalStationCode = arrivalStationCode;
		this.departureDateTime = departureDateTime;
		this.arrivalDateTime = arrivalDateTime;
		this.TXTInfo = "";
	}

	@Override
	protected TimeTable scrape(final TagNode rootNode) {
		final TimeTable table = new TimeTable(departureStationCode, arrivalStationCode, departureDateTime, arrivalDateTime);
		
		try {
			// Build time table
			final Object[] nodes = rootNode.evaluateXPath(TABLE_ROW_XPATH);
			for (int i = 2; i < nodes.length; i++) { // The firsts are headers
				final List<TagNode> columns = ((TagNode) nodes[i]).getChildTagList();
				
				final String tripLine = columns.get(0).getText().toString();
				
				final String[] tripDepartureTime = columns.get(1).getText().toString().split("\\."); // Format is HH.mm
				final String[] tripArrivalTime = columns.get(2).getText().toString().split("\\."); // Format is HH.mm

				// To match HH:mm
				final String[] tripTimeArray = columns.get(3).getText().toString().split("\\.");
				final String tripTime = String.format("%02d:%02d", Integer.parseInt(tripTimeArray[0]), Integer.parseInt(tripTimeArray[1]));
				
				final boolean civis = columns.get(4).getText().toString().equalsIgnoreCase("civis");
				
				final Calendar tripDepartureDateTime = Calendar.getInstance(TimeZone.getTimeZone("GMT+02"));
				tripDepartureDateTime.setTimeInMillis(departureDateTime.getTimeInMillis()); // Will be the same day of departureDateTime
				tripDepartureDateTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(tripDepartureTime[0])); // Hours
				tripDepartureDateTime.set(Calendar.MINUTE, Integer.parseInt(tripDepartureTime[1])); // Minutes
				
				final Calendar tripArrivalDateTime = Calendar.getInstance(TimeZone.getTimeZone("GMT+02"));
				tripArrivalDateTime.setTimeInMillis(departureDateTime.getTimeInMillis()); // Will be the same day of departureDateTime
				tripArrivalDateTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(tripArrivalTime[0])); // Hours
				tripArrivalDateTime.set(Calendar.MINUTE, Integer.parseInt(tripArrivalTime[1])); // Minutes
				
				table.addTrip(new Trip(tripLine, tripDepartureDateTime, tripArrivalDateTime, tripTime, civis));
			}
			
		} catch (XPatherException e) {
			Log.e(TAG, "XPatherException: " + e.getMessage());
			throw new RuntimeException("XPatherException: " + e.getMessage());
		} catch (NumberFormatException e) {
			Log.e(TAG, "NumberFormatException parsing hours to int: " + e.getMessage());
			throw new RuntimeException("NumberFormatException parsing hours to int: " + e.getMessage());
		}
		
		return table;
	}

	@Override
	protected String getWebpage() {
		final SimpleDateFormat dateSdf = DateUtils.newSimpleDateFormatES("yyyyMMdd");
		final SimpleDateFormat hourSdf = DateUtils.newSimpleDateFormatES("HH");
		
		final Form form = new Form();
		
		form.param(REGION_CODE_PARAM_NAME, regionCode);
		form.param(I_PARAM_NAME, i);
		form.param(CP_PARAM_NAME, cp);
		form.param(FROM_PARAM_NAME, departureStationCode);
		form.param(TO_PARAM_NAME, arrivalStationCode);
		form.param(DATE_PARAM_NAME, dateSdf.format(departureDateTime.getTime()));
		form.param(DEPARTURE_TIME_PARAM_NAME, hourSdf.format(departureDateTime.getTime()));
		form.param(ARRIVAL_TIME_PARAM_NAME, hourSdf.format(arrivalDateTime.getTime()));
		form.param(INFO_PARAM_NAME, TXTInfo);
		
		return WebExecutor.syncPostRequest(BASE_URL, form);
	}

}
