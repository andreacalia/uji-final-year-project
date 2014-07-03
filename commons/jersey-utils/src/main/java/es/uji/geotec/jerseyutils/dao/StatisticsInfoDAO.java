package es.uji.geotec.jerseyutils.dao;

import org.jongo.MongoCollection;

import es.uji.geotec.commonutils.Log;
import es.uji.geotec.jerseyutils.message.ErrorMessages;
import es.uji.geotec.jerseyutils.model.StatisticInfo;

public class StatisticsInfoDAO extends AbstractJongoDAO {

	private static final String TAG = StatisticsInfoDAO.class.getName();
	private static final String BICICAS_DB_NAME = "bicicas";

	public static final String STATISTICS_COLLECTION = "statistics";

	private MongoCollection statisticsCollection;
	
	public StatisticsInfoDAO() {
		super(BICICAS_DB_NAME, AbstractJongoDAO.DEFAULT_SERVER_HOST, AbstractJongoDAO.DEFAULT_SERVER_PORT);
	}
	
	@Override
	public void link() {
		super.link();
		statisticsCollection = jongo.getCollection(STATISTICS_COLLECTION);
	};
	
	public boolean insert(final StatisticInfo info) {
		super.assertLinked();
		
		final String err = statisticsCollection.insert(info).getError();
		if( err != null ) {
			Log.e(TAG, ErrorMessages.STATISTICS_INSERT_ERROR, err);
			return false;
		}
		return true;
	}
}
