package es.uji.tripplanner.controller;

import java.util.Calendar;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import es.uji.geotec.commondatamodel.GeoJSONPoint;
import es.uji.geotec.commondatamodel.GeoJSONRoute;
import es.uji.geotec.commonutils.DateUtils;
import es.uji.geotec.commonutils.Log;
import es.uji.geotec.commonutils.XSLTUtils;
import es.uji.geotec.commonutils.expection.XSLTException;
import es.uji.geotec.httputils.RouteServiceHelper;
import es.uji.geotec.httputils.WebExecutor;
import es.uji.geotec.httputils.exception.RouteServiceException;
import es.uji.tripplanner.messages.ErrorMessages;
import es.uji.tripplanner.utils.Paths;

@Path(Paths.URL.AJAX_BASE)
public class AjaxController {

	private static final String TEST_MODE_ON = "on";
	private static final String TEST_MODE_OFF = "off";
	private static final String TAG = AjaxController.class.getName();

	@GET
	@Path(Paths.URL.TRAIN_STATION_LOCATION)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTrainStationLocation() {
		Log.i(TAG, "getTrainStationLocation");
		
		final double lat = 39.98823;
		final double lon = -0.05246;
		
		final GeoJSONPoint point = new GeoJSONPoint();
		point.coordinates[0] = lat;
		point.coordinates[1] = lon;
		
		return Response.ok(new Gson().toJson(point)).build();
	}
	
	@GET
	@Path(Paths.URL.TRAIN_TIME_TABLE)
	@Produces(MediaType.TEXT_HTML)
	public Response getTrainStationTimeTable(
			@QueryParam("millisFrom") final Long millisFrom,
			@QueryParam("millisTo") final Long millisTo) {
		Log.i(TAG, "getTrainStationTimeTable");
		
		final Calendar fromCalendar = DateUtils.newCalendarES();
		final Calendar toCalendar = DateUtils.newCalendarES();
		
		fromCalendar.setTimeInMillis(millisFrom);
		toCalendar.setTimeInMillis(millisTo);
		
		final int castellonTrainStationCode = 65000;
		final int valenciaTrainStationCode = 65300;
		
		final String trainTimetableURL = String.format(
				Paths.URL.RENFE_WEB_SERVICE_BASE_URL+"ws/timetable/%d/%04d/%02d/%02d/%02d/%d/%04d/%02d/%02d/%02d?semantic=h-event",
				castellonTrainStationCode,
				fromCalendar.get(Calendar.YEAR),
				fromCalendar.get(Calendar.MONTH)+1,
				fromCalendar.get(Calendar.DAY_OF_MONTH),
				fromCalendar.get(Calendar.HOUR_OF_DAY),
				valenciaTrainStationCode,
				toCalendar.get(Calendar.YEAR),
				toCalendar.get(Calendar.MONTH)+1,
				toCalendar.get(Calendar.DAY_OF_MONTH),
				toCalendar.get(Calendar.HOUR_OF_DAY));
		
		Log.i(TAG, "Train time table URL: "+trainTimetableURL);

		try {
			String xml = WebExecutor.syncGetRequest(trainTimetableURL, MediaType.APPLICATION_XML_TYPE);
			xml = XSLTUtils.applyXSLT(xml, Paths.XSLT.TRAIN_TIME_TABLE_XML_TO_HTML);
			
			return Response.ok(xml).build();
			
		} catch (XSLTException e) {
			Log.e(TAG, e, ErrorMessages.XSLT_ERROR, Paths.XSLT.TRAIN_TIME_TABLE_XML_TO_HTML);
		}
		
		return Response.status(500).build();
	}

	@GET
	@Path(Paths.URL.NEAREST_BICYCLE_POINT_LOCATION)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNearestBicyclePoint(
			@QueryParam("lon") final String lon,
			@QueryParam("lat") final String lat,
			@QueryParam("testMode") @DefaultValue(TEST_MODE_OFF) final String testMode) {
		Log.i(TAG, "getNearestBicyclePoint");
		
		if( testMode.equals(TEST_MODE_ON) ) {
			Log.i(TAG, "Test mode on");
			
			final String url = Paths.URL.BICICAS_WEB_SERVICE_BASE_URL+"ws/bicicas/40";
			final String json = WebExecutor.syncGetRequest(url, MediaType.APPLICATION_JSON_TYPE);

			return Response.ok(json).build();
		}
		
		final String url = Paths.URL.BICICAS_WEB_SERVICE_BASE_URL+"ws/bicicas/nearest";
		
		try {
			final WebTarget target = ClientBuilder.newClient()
					.target(url)
					.queryParam("lon", lon)
					.queryParam("lat", lat);

			// Return the bicicas point directly, in json
			final String json = WebExecutor.syncGetRequest(target, MediaType.APPLICATION_JSON_TYPE);
			return Response.ok(json).build();
			
		} catch (Exception e) {
			Log.e(TAG, e, ErrorMessages.CONNECTION_ERROR, (url));
		}

		return Response.serverError().build();
	}
	
	@GET
	@Path(Paths.URL.ROUTE)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRoute(
			@QueryParam("flon") final String flon,
			@QueryParam("flat") final String flat,
			@QueryParam("tlon") final String tlon,
			@QueryParam("tlat") final String tlat,
			@DefaultValue("bicycle") @QueryParam("v") final String v) {
		Log.i(TAG, "getRoute");
		
		try {
			
			final GeoJSONRoute route = RouteServiceHelper.getRoute(flon, flat, tlon, tlat, v);
			return Response.ok(new Gson().toJson(route)).build();
			
		} catch (RouteServiceException e) {
			Log.e(TAG, ErrorMessages.ROUTE_SERVICE_ERROR, e);
		}
		
		return Response.serverError().build();
	}
}
