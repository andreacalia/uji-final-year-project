package es.uji.geotec.httputils.message;

public class ErrorMessages {

	public static final String INTERRUPTED_ERROR = "Thread interrupted exception";
	public static final String WEB_CLIENT_PROCESS_RESPONSE_FAIL = "Failed to process HTTP response";
	public static final String WEB_CLIENT_IO_ERROR = "I/O error processing the response";
	public static final String WEB_CLIENT_RESPONSE_CODE_ERROR = "Server response status code is not successful";
	public static final String WEB_EXECUTOR_IS_SHUTTED_DOWN = "The web executor is shutted down";
	public static final String ROUTE_SERVICE_COORDINATE_ERROR = "Error getting the coordinates from the route service";
	public static final String ROUTE_SERVICE_PARSER_CONFIGURATION_ERROR = "Error parsing the content of the route service response";
	public static final String ROUTE_SERVICE_SAX_ERROR = "SAX error parsing the route serice response";
	public static final String IO_ERROR = "IO Exception";
	public static final String ROUTE_SERVICE_XPATH_ERROR = "XPath error in the route service";
}
