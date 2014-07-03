package es.uji.renfewebservice.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import es.uji.geotec.commonutils.DateUtils;
import es.uji.geotec.commonutils.Log;
import es.uji.renfewebservice.scraping.RenfeWebpageScraper;

/**
 * This class perform the computation needed to parse and compute the Renfe's web page and build a TimeTable.
 *
 */
public class RenfeTimeTableBuilder {
	
	private static final String TAG = RenfeTimeTableBuilder.class.getName();
	
	private static final String MIN_HOUR_OF_DAY = "00";
	private static final String MIN_MINUTES_OF_DAY = "00";
	private static final String MAX_HOUR_OF_DAY = "23";
	private static final String MAX_MINUTES_OF_DAY = "59";
	
	private String departureStationCode;
	private String arrivalStationCode;
	private Calendar departureDateTime;
	private Calendar arrivalDateTime;
	
	public RenfeTimeTableBuilder(final String departureStationCode, final String arrivalStationCode, final String departureDate, final String arrivalDate) {
		this(departureStationCode, arrivalStationCode, departureDate, MIN_HOUR_OF_DAY, arrivalDate, MAX_HOUR_OF_DAY);
	}

	public RenfeTimeTableBuilder(final String departureStationCode, final String arrivalStationCode,
			final String departureDate, final String departureHour,
			final String arrivalDate, final String arrivalHour) {
		super();
		this.departureStationCode = departureStationCode;
		this.arrivalStationCode = arrivalStationCode;
		
		// TODO: Check format with regex
		// Build calendars
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.ITALY); // Italy locale is the same of Spain
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+02")); // Spain timezone
		
		final Calendar departureDateTime = Calendar.getInstance(TimeZone.getTimeZone("GMT+02")); // Spain timezone
		final Calendar arrivalDateTime = Calendar.getInstance(TimeZone.getTimeZone("GMT+02")); // Spain timezone
		
		try {
			departureDateTime.setTime(sdf.parse(departureDate));
			arrivalDateTime.setTime(sdf.parse(arrivalDate));
		} catch (ParseException e) {
			Log.e(TAG, "ParseException parsing date from string: " + e.getMessage());
			throw new RuntimeException("ParseException parsing date from string: " + e.getMessage());
		}
		
		final int departureHourInt = Integer.parseInt(departureHour);
		final int arrivalHourInt = Integer.parseInt(arrivalHour);
		
		// Check valid hours
		if( departureHourInt < 0 || departureHourInt > 23 ) {
			Log.e(TAG, "Departure hour must be between 00 and 23");
			throw new RuntimeException("Departure hour must be between 00 and 23");
		}
		if( arrivalHourInt < 0 || arrivalHourInt > 23 ) {
			Log.e(TAG, "Arrival hour must be between 00 and 23");
			throw new RuntimeException("Arrival hour must be between 00 and 23");
		}
		
		departureDateTime.set(Calendar.HOUR_OF_DAY, departureHourInt);
		departureDateTime.set(Calendar.MINUTE, 0);
		
		arrivalDateTime.set(Calendar.HOUR_OF_DAY, arrivalHourInt);
		arrivalDateTime.set(Calendar.MINUTE, 59);
		
		// Check valid dates
		if( departureDateTime.after(arrivalDateTime) ) {
			Log.e(TAG, "Departure date cannot be after arrival date");
			throw new RuntimeException("Departure date cannot be after arrival date");
		}
		
		this.departureDateTime = departureDateTime;
		this.arrivalDateTime = arrivalDateTime;
	}

	public TimeTable getTimeTableConcurrent() {
		TimeTable finalTable = null;
		
		// If the trip start and end in the same day:
		if (departureDateTime.get(Calendar.DAY_OF_YEAR) == arrivalDateTime.get(Calendar.DAY_OF_YEAR) &&
				departureDateTime.get(Calendar.YEAR) == arrivalDateTime.get(Calendar.YEAR)) {
			// No problem, the request is simple
			try {
				finalTable = new RenfeWebpageScraper(departureStationCode, arrivalStationCode, departureDateTime, arrivalDateTime).performScraping();
			} catch (Exception e) {
				Log.e(TAG, "An exception occurred during scraping: " + e.getMessage());
				e.printStackTrace();
				throw new RuntimeException("An exception occurred during scraping: " + e.getMessage());
			}
		} else {
			// Otherwise, various requests are performed and the results are concatenated
			finalTable = new TimeTable(departureStationCode, arrivalStationCode, departureDateTime, arrivalDateTime);
			
			// This save the different tuple of departure/arrival needed to accomplish the timetable.
			// Example:
			// departure: 10/10/2014 17:00
			// arrival: 12/10/2014 14:00
			// [0]:
			// 		[0] => 10/10/2014 17:00
			// 		[1] => 10/10/2014 23:59
			// [1]:
			// 		[0] => 11/10/2014 00:00
			// 		[1] => 11/10/2014 23:59
			// [2]:
			// 		[0] => 12/10/2014 00:00
			// 		[1] => 12/10/2014 14:00
			final List<Calendar[]> trips = new ArrayList<Calendar[]>();
			
			Calendar tempDepartureDateTime = null;
			Calendar tempArrivalDateTime = null;
			
			// The first request is from departuredatetime to departuredateT23:59
			tempDepartureDateTime = DateUtils.newCalendarES();
			tempArrivalDateTime = DateUtils.newCalendarES();
			tempDepartureDateTime.setTimeInMillis(departureDateTime.getTimeInMillis());
			tempArrivalDateTime.setTimeInMillis(departureDateTime.getTimeInMillis());
			tempArrivalDateTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(MAX_HOUR_OF_DAY));
			tempArrivalDateTime.set(Calendar.MINUTE, Integer.parseInt(MAX_MINUTES_OF_DAY));
			trips.add(new Calendar[]{tempDepartureDateTime, tempArrivalDateTime});
			
			// The requests between are from departuredate{+n}T00:00 to departureDate{+n}T23.59
			tempDepartureDateTime = DateUtils.newCalendarES();
			tempArrivalDateTime = DateUtils.newCalendarES();
			tempDepartureDateTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(MIN_HOUR_OF_DAY));
			tempDepartureDateTime.set(Calendar.MINUTE, Integer.parseInt(MIN_MINUTES_OF_DAY));
			tempDepartureDateTime.add(Calendar.DAY_OF_MONTH, 1);
			tempArrivalDateTime.add(Calendar.DAY_OF_MONTH, 1);
			
			while( tempDepartureDateTime.get(Calendar.DAY_OF_MONTH) != arrivalDateTime.get(Calendar.DAY_OF_MONTH) ) {
				
				trips.add(new Calendar[]{tempDepartureDateTime, tempArrivalDateTime});
				
				tempDepartureDateTime.add(Calendar.DAY_OF_MONTH, 1);
				tempArrivalDateTime.add(Calendar.DAY_OF_MONTH, 1);
			}
			
			// The last request is from arrivaldateT00:00 to arrivaldate
			trips.add(new Calendar[]{tempDepartureDateTime, arrivalDateTime}); // At this point tempDepartureDateTime = arrivaldateT00:00
			
			// Now perform the requests
			final ExecutorService executor = Executors.newFixedThreadPool(Math.max(trips.size(), Runtime.getRuntime().availableProcessors() * 2));
		    final List<Future<TimeTable>> futures = new ArrayList<Future<TimeTable>>();
			for( int i = 0; i < trips.size(); i++ ) {
				final int index = i;
				final Callable<TimeTable> task = new Callable<TimeTable>() {
					@Override
					public TimeTable call() throws Exception {
						return new RenfeWebpageScraper(departureStationCode, arrivalStationCode, trips.get(index)[0], trips.get(index)[1]).performScraping();
					}
				};
				futures.add(executor.submit(task));
			}
			
			executor.shutdown();
			
			for( Future<TimeTable> future : futures ) {
				try {
					
					final TimeTable tempTimeTable = future.get();
					concatenateTrips(tempTimeTable, finalTable);
					
				} catch (InterruptedException | ExecutionException e) {
					Log.e(TAG, "An exception occurred during scraping: " + e.getMessage());
					e.printStackTrace();
					throw new RuntimeException("An exception occurred during scraping: " + e.getMessage());
				}
			}
			
		}
		return finalTable;
	}
	
	private void concatenateTrips(final TimeTable sourceTable, final TimeTable destTable) {
		destTable.getTrips().addAll(sourceTable.getTrips());
	}
	
}
