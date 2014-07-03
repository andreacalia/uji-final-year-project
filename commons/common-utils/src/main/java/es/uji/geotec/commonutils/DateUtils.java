package es.uji.geotec.commonutils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Allow to get Date instances suitable for Spain.
 *
 */
public class DateUtils {

	private static final Calendar spanishCalendar = Calendar.getInstance();
	
	static {
		spanishCalendar.setTimeZone(TimeZone.getTimeZone("GMT+02")); // Spain timezone
		spanishCalendar.clear(); // All properties reset
	}
	
	public static Date newDateES() {
		return ((Calendar) spanishCalendar.clone()).getTime();
	}
	
	public static Calendar newCalendarES() {
		return (Calendar) spanishCalendar.clone();
	}
	
	public static Date nowDateES() {
		return Calendar.getInstance(TimeZone.getTimeZone("GMT+02")).getTime(); // Spain timezone
	}
	
	public static Calendar nowCalendarES() {
		return Calendar.getInstance(TimeZone.getTimeZone("GMT+02")); // Spain timezone
	}
	
	public static SimpleDateFormat newSimpleDateFormatES(final String pattern) {
		return new SimpleDateFormat(pattern, Locale.forLanguageTag("es-ES"));
	}
}
