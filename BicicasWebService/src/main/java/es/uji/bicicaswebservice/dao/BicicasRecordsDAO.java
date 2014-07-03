package es.uji.bicicaswebservice.dao;

import org.jongo.MongoCollection;

import es.uji.bicicasdatamodel.BicicasPointRecord;
import es.uji.bicicaswebservice.messages.ErrorMessages;
import es.uji.geotec.commonutils.Log;

/**
 * DAO to interact with the MongoDB Collection of bicicas
 *
 */
public final class BicicasRecordsDAO extends AbstractJongoDAO {

	private static final String TAG = BicicasRecordsDAO.class.getName();
	private static final String BICICAS_DB_NAME = "bicicas";

	public static final String BICICAS_RECORDS_COLLECTION = "records";
	
	private MongoCollection bicicasRecordsCollection;
	
	public BicicasRecordsDAO() {
		super(BICICAS_DB_NAME, AbstractJongoDAO.DEFAULT_SERVER_HOST, AbstractJongoDAO.DEFAULT_SERVER_PORT);
	}
	
	@Override
	public void link() {
		super.link();
		bicicasRecordsCollection = jongo.getCollection(BICICAS_RECORDS_COLLECTION);
	};
	
	public boolean insert(final BicicasPointRecord bicicasRecordPoint) {
		super.assertLinked();
		
		final String err = bicicasRecordsCollection.insert(bicicasRecordPoint).getError();
		if( err != null ) {
			Log.e(TAG, ErrorMessages.MONGO_UPDATE_ERROR, err);
			return false;
		}
		return true;
	}
	
	public void dropCollection() {
		super.assertLinked();
		
		bicicasRecordsCollection.drop();
	}

}
