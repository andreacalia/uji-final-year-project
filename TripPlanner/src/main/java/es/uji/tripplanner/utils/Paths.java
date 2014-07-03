package es.uji.tripplanner.utils;

public final class Paths {
	
	public static final class Template {
		
		public static final String TEMPLATES_BASE_PATH = Paths.class.getResource("/templates").getPath();
		public static final String INDEX = "/index.ftl";
	}

	public static final class URL {

		private static final String HOST = "http://localhost:8080";
		public static final String RENFE_WEB_SERVICE_BASE_URL = HOST + "/RenfeWebService/";
		public static final String BICICAS_WEB_SERVICE_BASE_URL = HOST + "/BicicasWebService/";
		
		public static final String ROOT = "/";
		public static final String AJAX_BASE = "/ajax/";
		
		public static final String INDEX = "index";
		public static final String NEAREST_BICYCLE_POINT = "/bicycles";
		public static final String TRAIN_STATION_LOCATION = "/location/trainstation";
		public static final String NEAREST_BICYCLE_POINT_LOCATION = "/location/bicicas/nearest";
		public static final String ROUTE = "/route";
		public static final String TRAIN_TIME_TABLE = "/train/timetable";
	}
	
	public static final class XSLT {
		
		public static final String TRAIN_TIME_TABLE_XML_TO_HTML = Paths.class.getResource("/xslt/trainTimeTableXSLT.xsl").getPath();
	}
}
