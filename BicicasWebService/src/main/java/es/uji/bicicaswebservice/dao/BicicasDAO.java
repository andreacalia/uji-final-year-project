package es.uji.bicicaswebservice.dao;

import java.util.Iterator;

import org.jongo.MongoCollection;

import es.uji.bicicasdatamodel.BicicasPoint;
import es.uji.bicicaswebservice.messages.ErrorMessages;
import es.uji.geotec.commonutils.Log;

/**
 * DAO to interact with the MongoDB Collection of bicicas
 *
 */
public final class BicicasDAO extends AbstractJongoDAO {

	private static final String TAG = BicicasDAO.class.getName();
	private static final String BICICAS_DB_NAME = "bicicas";

	public static final String BICICAS_COLLECTION = "bicicas";

	private MongoCollection bicicasCollection;
	
	public BicicasDAO() {
		super(BICICAS_DB_NAME, AbstractJongoDAO.DEFAULT_SERVER_HOST, AbstractJongoDAO.DEFAULT_SERVER_PORT);
	}
	
	@Override
	public void link() {
		super.link();
		bicicasCollection = jongo.getCollection(BICICAS_COLLECTION);
	};
	
	public BicicasPoint getBicicasPointByCode(final Integer code) {
		super.assertLinked();
		
		return bicicasCollection.findOne("{code:#}", code).as(BicicasPoint.class);
	}
	
	public Iterator<BicicasPoint> getAllBicicasPoints() {
		super.assertLinked();
		
		return bicicasCollection.find().sort("{code:1}").as(BicicasPoint.class).iterator();
	}
	
	public Iterator<BicicasPoint> getAllAvailableBicicasPoints() {
		super.assertLinked();
		
		return bicicasCollection.find("{currentState.bicycleAvailable : {$gt: 0}}").sort("{code:1}").as(BicicasPoint.class).iterator();
	}
	
	public Iterator<BicicasPoint> getAllBicicasPointsNearTo(final Double lon, final Double lat, final int num) {
		super.assertLinked();
		
		return bicicasCollection.find("{geoJson:{$near: { $geometry: {type:\"Point\", coordinates: [#,#]} } } }", lon, lat).limit(num).as(BicicasPoint.class).iterator();
	}
	
	public Iterator<BicicasPoint> getAllBicicasPointsNearToWithAvailableBikes(final Double lon, final Double lat, final int num) {
		super.assertLinked();
		
		return bicicasCollection.find("{$and: [{geoJson:{$near: { $geometry: {type:\"Point\", coordinates: [#,#]} } } }, {currentState.bicycleAvailable : {$gt: 0}}]}", lon, lat).limit(num).as(BicicasPoint.class).iterator();
	}
	
	public boolean insert(final BicicasPoint bicicasPoint) {
		super.assertLinked();
		
		final String err = bicicasCollection.insert(bicicasPoint).getError();
		if( err != null ) {
			Log.e(TAG, ErrorMessages.BICICAS_DAO_ERROR_INSERTING_DATA, err);
			return false;
		}
		return true;
	}
	
	public boolean update(final Integer code, final BicicasPoint updatedData) {
		super.assertLinked();
		
		final String err = bicicasCollection.update("{'code':#}", code).upsert().with(updatedData).getError();
		if( err != null ) {
			Log.e(TAG, ErrorMessages.MONGO_UPDATE_ERROR, err);
			return false;
		}
		return true;
	}
	
	public void dropCollection() {
		super.assertLinked();
		
		bicicasCollection.drop();
	}

	public boolean insertBulkJSONString(final String jsonDataSet) {
		super.assertLinked();

		final String err = bicicasCollection.insert(jsonDataSet).getError();
		if( err != null ) {
			Log.e(TAG, ErrorMessages.MONGO_UPDATE_ERROR, err);
			return false;
		}
		return true;
	}

	public void ensureIndexes() {
		super.assertLinked();
		
		bicicasCollection.ensureIndex("{geoJson: '2dsphere'}");
	}
	
}
