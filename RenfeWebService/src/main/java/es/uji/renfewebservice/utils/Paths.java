package es.uji.renfewebservice.utils;

public final class Paths {
	
	/*
	public static final class Template {
		public static final String TEMPLATES_BASE_PATH = "templates";
	}
	*/
	
	public static class URL {
		public static final String TIMETABLE_WS = "timetable";
	}
	
	public static class XSLT {
		public static final String TIMETABLE_WS_HEVENT = Paths.class.getResource("/xslt/TimeTableHEvent.xsl").getPath();
		public static final String TIMETABLE_WS_RDFa = Paths.class.getResource("/xslt/TimeTableRDFa.xsl").getPath();
	}
}
