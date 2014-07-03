package es.uji.bicicaswebservice.dao;

import java.net.UnknownHostException;

import org.jongo.Jongo;

import com.mongodb.DB;
import com.mongodb.MongoClient;

import es.uji.bicicaswebservice.messages.ErrorMessages;
import es.uji.geotec.commonutils.Log;


/**
 * Abstract Jongo DAO. Provides some common methods 
 *
 */
public abstract class AbstractJongoDAO {

	private static final String TAG = AbstractJongoDAO.class.getName();
	
	public static final String DEFAULT_SERVER_HOST = "localhost";
	public static final int DEFAULT_SERVER_PORT = 27017;
	
	protected DB db;
	protected MongoClient mongoClient;
	protected Jongo jongo;
	
	private volatile boolean isLinked = false;
	private String dbName;
	private String serverHost;
	private int serverPort;

	protected AbstractJongoDAO(final String dbName, final String serverHost, final int serverPort) {
		this.dbName = dbName;
		this.serverHost = serverHost;
		this.serverPort = serverPort;
	}
	
	public void link() {
		try {
			// TODO: Mongo client can be shared application wide (it's thread-safe). It could be an improvement.
			mongoClient = new MongoClient(serverHost, serverPort);
			db = mongoClient.getDB(dbName);
			jongo = new Jongo(db);
			isLinked = true;
		} catch (UnknownHostException e) {
			Log.e(TAG, e, ErrorMessages.MONGO_CONNECTION_ERROR);
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public void unlink() {
		mongoClient.close();
		isLinked = false;
	}
	
	protected void assertLinked() {
		if( !isLinked )
			throw new RuntimeException(ErrorMessages.MONGO_MUST_BE_LINKED);
	}
}
