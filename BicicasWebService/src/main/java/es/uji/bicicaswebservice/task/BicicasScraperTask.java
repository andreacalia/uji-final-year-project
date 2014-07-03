package es.uji.bicicaswebservice.task;

import java.util.List;

import es.uji.bicicasdatamodel.BicicasPoint;
import es.uji.bicicasdatamodel.BicicasPointRecord;
import es.uji.bicicaswebservice.dao.BicicasDAO;
import es.uji.bicicaswebservice.dao.BicicasRecordsDAO;
import es.uji.bicicaswebservice.messages.ErrorMessages;
import es.uji.bicicaswebservice.model.BicicasPointUtils;
import es.uji.bicicaswebservice.scraping.BicicasScraper;
import es.uji.bicicaswebservice.utils.Paths;
import es.uji.geotec.commonutils.Log;
import es.uji.geotec.scheduledexecutorframework.task.AbstractScheduledTask;

/**
 * Task that perform the scraping of the Bicicas's webpage
 *
 */
public class BicicasScraperTask extends AbstractScheduledTask {

	private static final String TAG = BicicasScraperTask.class.getName();
	private static final String NAME = "bicicas";
	
	public BicicasScraperTask() {
		super(NAME);
	}

	@Override
	protected void doJob() {
		final BicicasScraper sc = new BicicasScraper(Paths.URL.BICICAS_WEBPAGE);
		final List<BicicasPoint> updatedBicicas = sc.performScraping();
		
		final BicicasDAO dao = new BicicasDAO();
		final BicicasRecordsDAO recordDao = new BicicasRecordsDAO();
		recordDao.link();
		dao.link();
		
		boolean result = true;
		int numUpdated = 0;
		
		for( final BicicasPoint updated : updatedBicicas ) {

			// Check if the bicicas point has changed
			final BicicasPoint current = dao.getBicicasPointByCode(updated.code);
			
			if( BicicasPointUtils.hasDockListStatusChanged(current, updated)) {
				
				final BicicasPointRecord record = BicicasPointUtils.buildRecord(current, updated);
				
				// Save the updated to the records
				result &= recordDao.insert(record);
				// Update the bicicas status
				result &= dao.update(updated.code, updated);
				
				numUpdated ++;
			}
				
		}
		
		dao.unlink();
		recordDao.unlink();
		
		if( numUpdated != 0 )
			Log.i(TAG, numUpdated + " bicicas point updated");
			
		if( ! result )
			Log.e(TAG, ErrorMessages.MONGO_ERROR_UPDATING_BICICAS_POINT);
	}

}
