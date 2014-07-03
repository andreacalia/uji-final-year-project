package es.uji.geotec.jerseyutils.message;

public class ErrorMessages {

	public static final String STATISTICS_INSERT_ERROR = "Error inserting statistics info [%s]";
	public static final String NO_CLIENT_IP_PROPERTY_SET = "The property of the client IP is not set. Please register the filter ClientIPTrackerFilter";

	public static final String MONGO_CONNECTION_ERROR = "Error connection to MongoDB server";
	public static final String MONGO_MUST_BE_LINKED = "A mongo link must be created before use it";
	
}
