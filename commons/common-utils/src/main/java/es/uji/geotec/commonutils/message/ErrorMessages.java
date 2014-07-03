package es.uji.geotec.commonutils.message;

public class ErrorMessages {

	public static final String INTERRUPTED_ERROR = "Thread interrupted exception";
	public static final String SCHEDULED_TASK_RUNNING_ERROR = "Error running a scheduled task";
	public static final String WEB_CLIENT_PROCESS_RESPONSE_FAIL = "Failed to process HTTP response";
	public static final String WEB_CLIENT_IO_ERROR = "I/O error processing the response";
	public static final String WEB_CLIENT_RESPONSE_CODE_ERROR = "Server response status code is not successful";
	public static final String WEB_CLIENT_URI_NOT_VALID = "The provided URI is not valid";
	public static final String WEB_EXECUTOR_IS_SHUTTED_DOWN = "The web executor is shutted down";
	public static final String ROUTE_SERVICE_COORDINATE_ERROR = "Error getting the coordinates from the route service";
	public static final String ROUTE_SERVICE_PARSER_CONFIGURATION_ERROR = "Error parsing the content of the route service response";
	public static final String ROUTE_SERVICE_SAX_ERROR = "SAX error parsing the route serice response";
	public static final String IO_ERROR = "IO Exception";
	public static final String ROUTE_SERVICE_XPATH_ERROR = "XPath error in the route service";
	public static final String JAXB_MARSHAL_ERROR = "Error during marshalling";
	public static final String MONGO_CONNECTION_ERROR = "Error connection to MongoDB server";
	public static final String MONGO_MUST_BE_INITIALIZED = "Mongo bicicas collection must be initialized and updated with bicicas status data";
	public static final String MONGO_MUST_BE_LINKED = "A mongo link must be created before use it";
	public static final String SCHEDULED_TASK_MANAGER_NOT_INIT = "Scheduled executor manager must be initialized before using it";
	public static final String STATISTICS_INSERT_ERROR = "Error inserting statistics info [%s]";
	public static final String NO_CLIENT_IP_PROPERTY_SET = "The property of the client IP is not set. Please register the filter ClientIPTrackerFilter";
	public static final String REFLECTION_ILLEGAL_ACCESS = "Could not instantiante object due to illegal access [%s]";
	public static final String REFLECTION_INSTANTIATION_ERROR = "Could not instantiante object because the class is abstract [%s]";
	public static final String REFLECTION_ILLEGAL_ARGUMENT = "Could not instantiate object because the number of parameters does not match the constructor [%s]";
	public static final String REFLECTION_INVOCATION_TARGET_ERROR = "Could not instantiate object because the constructor [%s] throw an exception";
	public static final String OBJECT_BUILDER_NEW_INSTANCE_ERROR = "Error creating an instance with ObjectBuilder";
}
