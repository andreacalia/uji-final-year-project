package es.uji.geotec.jerseyutils.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class StatisticInfo {
	
	public int ms;
	public String URI;
	public Boolean success;
	public String exceptionMessage;
	public Integer httpStatus;
	public Map<String, List<String>> headers;
	public Date date;
	public String clientIP;
	
}
