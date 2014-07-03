package es.uji.renfewebservice.controller;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import es.uji.geotec.commonutils.JAXBUtils;
import es.uji.geotec.commonutils.Log;
import es.uji.geotec.commonutils.XSLTUtils;
import es.uji.renfewebservice.model.RenfeTimeTableBuilder;
import es.uji.renfewebservice.model.TimeTable;
import es.uji.renfewebservice.utils.Paths;

@Path("/timetable/")
public class TimeTableController {
	
	private static final String TAG = TimeTableController.class.getName();
	private static final String SEMANTIC_NONE = "none";
	private static final String SEMANTIC_H_EVENT = "h-event";
	private static final String SEMANTIC_RDFa = "rdfa";

	@GET
	@Produces( MediaType.APPLICATION_XML )
	@Path("{departure}/{departureYear:\\d{4}}/{departureMonth:\\d{2}}/{departureDay:\\d{2}}/{departureTime:\\d{2}}"
			+ "/{arrival}/{arrivalYear:\\d{4}}/{arrivalMonth:\\d{2}}/{arrivalDay:\\d{2}}/{arrivalTime:\\d{2}}")
	public Response consultaHorarios(
			@PathParam("departure") final String departure,
			@PathParam("departureYear") final String departureYear,
			@PathParam("departureMonth") final String departureMonth,
			@PathParam("departureDay") final String departureDay,
			@PathParam("departureTime") @DefaultValue("00") final String departureTime,
			@PathParam("arrival") final String arrival,
			@PathParam("arrivalYear") final String arrivalYear,
			@PathParam("arrivalMonth") final String arrivalMonth,
			@PathParam("arrivalDay") final String arrivalDay,
			@PathParam("arrivalTime") @DefaultValue("23") final String arrivalTime,
			@QueryParam("semantic") @DefaultValue(SEMANTIC_NONE) final String semantic) {
		
		final String departureDate = departureYear + departureMonth + departureDay;
		final String arrivalDate = arrivalYear + arrivalMonth + arrivalDay;
		
		Log.i(TAG, "Requested scraping. From " + departureDate + " to " + arrivalDate + ". Semantic " + semantic);
		
		try {
			final RenfeTimeTableBuilder builder = new RenfeTimeTableBuilder(departure, arrival, departureDate, departureTime, arrivalDate, arrivalTime);
			final TimeTable timeTable = builder.getTimeTableConcurrent();
			
			// Create a XML String from TimeTable class
			String xmlResponse = JAXBUtils.marshal(timeTable);
			
			// Apply XSLT transform
			if( semantic.equals(SEMANTIC_H_EVENT) )
				xmlResponse = XSLTUtils.applyXSLT(xmlResponse, Paths.XSLT.TIMETABLE_WS_HEVENT);
			else if( semantic.equals(SEMANTIC_RDFa) )
				xmlResponse = XSLTUtils.applyXSLT(xmlResponse, Paths.XSLT.TIMETABLE_WS_RDFa);
				
			return Response.ok(xmlResponse).build();
		} catch (Exception e) {
			Log.e(TAG, "Error calculating time table: " + e.getMessage());
			return Response.serverError().build();
		}
	}
	
	@GET
	@Produces( MediaType.APPLICATION_JSON )
	@Path("{departure}/{departureYear:\\d{4}}/{departureMonth:\\d{2}}/{departureDay:\\d{2}}/{departureTime:\\d{2}}"
			+ "/{arrival}/{arrivalYear:\\d{4}}/{arrivalMonth:\\d{2}}/{arrivalDay:\\d{2}}/{arrivalTime:\\d{2}}")
	public Response getTimeTableJson(
			@PathParam("departure") final String departure,
			@PathParam("departureYear") final String departureYear,
			@PathParam("departureMonth") final String departureMonth,
			@PathParam("departureDay") final String departureDay,
			@PathParam("departureTime") @DefaultValue("00") final String departureTime,
			@PathParam("arrival") final String arrival,
			@PathParam("arrivalYear") final String arrivalYear,
			@PathParam("arrivalMonth") final String arrivalMonth,
			@PathParam("arrivalDay") final String arrivalDay,
			@PathParam("arrivalTime") @DefaultValue("23") final String arrivalTime) {
		
		final String departureDate = departureYear + departureMonth + departureDay;
		final String arrivalDate = arrivalYear + arrivalMonth + arrivalDay;
		try {
			final RenfeTimeTableBuilder builder = new RenfeTimeTableBuilder(departure, arrival, departureDate, departureTime, arrivalDate, arrivalTime);
			final TimeTable t = builder.getTimeTableConcurrent();
			return Response.ok(t).build();
		} catch (Exception e) {
			Log.e(TAG, "Error calculating time table: " + e.getMessage());
			return Response.serverError().build();
		}
	}

}
