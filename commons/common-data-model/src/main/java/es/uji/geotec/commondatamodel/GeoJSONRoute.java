package es.uji.geotec.commondatamodel;

import java.util.ArrayList;
import java.util.List;

public class GeoJSONRoute {

	public String id = "Route";
	public Geometry geometry = new Geometry();
	public Properties properties = new Properties();
	
	public class Geometry  {
		public String type = "LineString";
		public List<double[]> coordinates = new ArrayList<double[]>();
	}; 
	
	public class Properties {
		public Double distance;
		public Double travelTime;
	};
}
