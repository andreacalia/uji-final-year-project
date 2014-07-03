package es.uji.bicicaswebservice.messages;

/**
 * Container for all the error messages of the application
 *
 */
public final class ErrorMessages {

	public static final String MONGO_MUST_BE_INITIALIZED = "Mongo bicicas collection must be initialized and updated with bicicas status data";
	public static final String ADMIN_INITIAL_DATASET_NOT_FOUND = "Initial dataset not found on disk";
	public static final String MONGO_UPDATE_ERROR = "Mongo error while updating data [%s]";
	public static final String MONGO_INIT_ERROR = "Error inserting initial dataset";

	public static final String SCRAPER_ERROR_XPATH = "Error applying XPath to the Bicicas's webpage";
	public static final String EXECUTOR_SHUTDOWN_INTERRUPTED = "Interrupted Exception while shutting down the executor";
	public static final String MONGO_ERROR_UPDATING_BICICAS_POINT = "An error occurred updating bicicas data";
	public static final String THREAD_INTERRUPTED_EXCEPTION = "An error interrupt error occurred";

	public static final String GENERIC_ERROR = "Error";
	public static final String DOCKS_LIST_MUST_HAVE_SAME_SIZE = "To compare two docks, they must have the same num of bikes docks";
	public static final String BICICAS_DAO_ERROR_INSERTING_DATA = "Error while inserting data";
	
	public static final String ROUTE_SERVICE_ERROR = "Error connecting to the route service";
	public static final String XSLT_ERROR = "Error applying XSLT transformation [%s]";

	public static final String MONGO_CONNECTION_ERROR = "Error connection to MongoDB server";
	public static final String MONGO_MUST_BE_LINKED = "A mongo link must be created before use it";
	
}
