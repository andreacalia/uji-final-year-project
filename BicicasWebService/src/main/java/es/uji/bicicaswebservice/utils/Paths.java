package es.uji.bicicaswebservice.utils;


/**
 * This class includes the paths used in the application
 *
 */
public class Paths {

	public static final class Assets {
		
		public static final String MONGO_INITIAL_DATA_SET = Paths.class.getResource("/data/initial-db-data.json").getPath();
		
	}

	public static final class URL {

		public static final String BICICAS_WEBPAGE = "http://www.bicicas.es/estado/EstadoActual.asp";
		
		public static final String ADMIN_BASE = "admin";

		public static final String CACHE_INIT = "cache/init";

		public static final String CACHE = "cache";

		public static final String TASKS_START_TASKS = "tasks/startTasks";
		
		public static final String TASKS_START_SERVICES = "tasks/startServices";

		public static final String TASKS_STOP_TASKS = "tasks/stopTasks";

		public static final String TASKS_STOP_SERVICES = "tasks/stopServices";

		public static final String TASKS = "tasks";

		public static final String TASK_PERIOD_CHANGE = "tasks/{name}";
		
		public static final String REST_BASE = "/";

		public static final String REST_BICICAS_POINT = "bicicas/{code}";

		public static final String REST_ALL_BICICAS = "bicicas";

		public static final String REST_NEAREST_BICICAS = "bicicas/nearest";

		
	}
	
	public static final class XSLT {
		public static final String BICICAS_HGEO = Paths.class.getResource("/xslt/BicicasHGeo.xsl").getPath();
	}
}
