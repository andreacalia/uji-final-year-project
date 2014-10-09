package es.uji.bicicaswebservice.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import es.uji.bicicasdatamodel.BicicasPoint;
import es.uji.bicicasdatamodel.BicicasPointList;
import es.uji.bicicaswebservice.dao.BicicasDAO;
import es.uji.bicicaswebservice.messages.ErrorMessages;
import es.uji.bicicaswebservice.utils.Paths;
import es.uji.geotec.commonutils.JAXBUtils;
import es.uji.geotec.commonutils.Log;
import es.uji.geotec.commonutils.XSLTUtils;
import es.uji.geotec.commonutils.expection.XSLTException;
import es.uji.geotec.httputils.RouteServiceHelper;
import es.uji.geotec.httputils.exception.RouteServiceException;

/**
 * REST controller offers the API to get info about bicicas
 *
 */
@Path(Paths.URL.REST_BASE)
public class RESTController {
	// TODO: Add HTTP auth
	
	private static final String TAG = RESTController.class.getName();
	private static final int NUM_NEAR_BICICAS_TO_EVALUATE = 3;

	@GET
	@Path(Paths.URL.REST_BICICAS_POINT)
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getBicicasPointByCode(@PathParam("code") Integer code, @Context HttpHeaders headers) {
		final BicicasDAO dao = new BicicasDAO();
		dao.link();
		
		final BicicasPoint point = dao.getBicicasPointByCode(code);
		
		dao.unlink();
		
		if( headers.getAcceptableMediaTypes().contains(MediaType.APPLICATION_JSON_TYPE) )
			// JSON is requested
			return Response.ok(point).build();

		// XML is requested
		String xml = JAXBUtils.marshal(point);
		try {
			xml = XSLTUtils.applyXSLT(xml, Paths.XSLT.BICICAS_HGEO);
		} catch (XSLTException e) {
			Log.e(TAG, e, ErrorMessages.XSLT_ERROR, Paths.XSLT.BICICAS_HGEO);
		}
		
		return Response.ok(xml).build();
	}
	
	@GET
	@Path(Paths.URL.REST_ALL_BICICAS)
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getAllBicicas(@Context HttpHeaders headers) {
		final BicicasDAO dao = new BicicasDAO();
		dao.link();
		
		final Iterator<BicicasPoint> it = dao.getAllBicicasPoints();
		final List<BicicasPoint> all = new ArrayList<>();
		
		while( it.hasNext() )
			all.add(it.next());
		
		dao.unlink();
		
		if( headers.getAcceptableMediaTypes().contains(MediaType.APPLICATION_JSON_TYPE) )
			// JSON is requested
			return Response.ok(all).build();

		// XML is requested
		// Wrap the list in an object because of JAXB
		final BicicasPointList list = new BicicasPointList();
		list.bicicasPoints = all;
		
		String xml = JAXBUtils.marshal(list);
		
		try {
			xml = XSLTUtils.applyXSLT(xml, Paths.XSLT.BICICAS_HGEO);
		} catch (XSLTException e) {
			Log.e(TAG, e, ErrorMessages.XSLT_ERROR, Paths.XSLT.BICICAS_HGEO);
		}
		
		return Response.ok(xml).build();
	}
	
	@GET
	@Path(Paths.URL.REST_NEAREST_BICICAS)
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getNearestBicyclePoint(
			@QueryParam("lon") final Double lon,
			@QueryParam("lat") final Double lat,
			@DefaultValue("bicycle") @QueryParam("v") final String v,
			@Context HttpHeaders headers) {
		
		Log.d(TAG, "getNearestBicyclePoint [lat:"+lat+", lon:"+lon+"]");
		final BicicasDAO dao = new BicicasDAO();
		dao.link();
		
		// Nearest bicicas points sorted by geometrical distance
		final Iterator<BicicasPoint> it = dao.getAllBicicasPointsNearTo(lon, lat, NUM_NEAR_BICICAS_TO_EVALUATE);
		
		// The formula to decide the nearest is: L = num_free_bikes / distance**2
		BicicasPoint nearest = null;
		double maxL = Double.MIN_VALUE;
		
		try {
			// For each bicicas point
			while( it.hasNext() ) {
				final BicicasPoint currentPoint = it.next();
				final int numFreeBikes = currentPoint.currentState.bicycleAvailable;
				final double distance = RouteServiceHelper.getRoute(
							lon.toString(), lat.toString(),
							currentPoint.location.lon.toString(), currentPoint.location.lat.toString(), v).properties.distance;
				
				final double currentL = numFreeBikes / (distance * distance);
				
				if( currentL > maxL ) {
					maxL = currentL;
					nearest = currentPoint;
				}
			}
			
		} catch (RouteServiceException e) {
			Log.e(TAG, e, ErrorMessages.ROUTE_SERVICE_ERROR);
		} catch (NullPointerException e) {
			Log.e(TAG, e, ErrorMessages.MONGO_MUST_BE_INITIALIZED);
		}
		
		dao.unlink();
		
		if( headers.getAcceptableMediaTypes().contains(MediaType.APPLICATION_JSON_TYPE) )
			// JSON is requested
			return Response.ok(nearest).build();

		// XML is requested
		String xml = JAXBUtils.marshal(nearest);
		try {
			xml = XSLTUtils.applyXSLT(xml, Paths.XSLT.BICICAS_HGEO);
		} catch (XSLTException e) {
			Log.e(TAG, e, ErrorMessages.XSLT_ERROR, Paths.XSLT.BICICAS_HGEO);
		}
		
		return Response.ok(xml).build();
	}
	
	@GET
	@Path(Paths.URL.REST_ALL_AVAILABLE_BICICAS)
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getAllAvailableBikes(@Context HttpHeaders headers) {
		final BicicasDAO dao = new BicicasDAO();
		dao.link();
		
		final Iterator<BicicasPoint> it = dao.getAllAvailableBicicasPoints();
		final List<BicicasPoint> all = new ArrayList<>();
		
		while( it.hasNext() )
			all.add(it.next());
		
		dao.unlink();
		
		if( headers.getAcceptableMediaTypes().contains(MediaType.APPLICATION_JSON_TYPE) )
			// JSON is requested
			return Response.ok(all).build();

		// XML is requested
		// Wrap the list in an object because of JAXB
		final BicicasPointList list = new BicicasPointList();
		list.bicicasPoints = all;
		
		String xml = JAXBUtils.marshal(list);
		
		try {
			xml = XSLTUtils.applyXSLT(xml, Paths.XSLT.BICICAS_HGEO);
		} catch (XSLTException e) {
			Log.e(TAG, e, ErrorMessages.XSLT_ERROR, Paths.XSLT.BICICAS_HGEO);
		}
		
		return Response.ok(xml).build();
	}
	
	@GET
	@Path(Paths.URL.REST_STRAIGHT_NEAREST_BICICAS)
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getStraightNearestBicyclePoint(
			@QueryParam("lon") final Double lon,
			@QueryParam("lat") final Double lat,
			@QueryParam("num") final Integer num,
			@Context HttpHeaders headers) {
		
		Log.d(TAG, "getNearestBicyclePoint [lat:"+lat+", lon:"+lon+"]");
		final BicicasDAO dao = new BicicasDAO();
		dao.link();
		
		// Nearest bicicas points sorted by geometrical distance
		final Iterator<BicicasPoint> it = dao.getAllBicicasPointsNearToWithAvailableBikes(lon, lat, num);
		final List<BicicasPoint> points = new ArrayList<BicicasPoint>();
		while( it.hasNext() )
			points.add(it.next());
		
		dao.unlink();
		
		if( headers.getAcceptableMediaTypes().contains(MediaType.APPLICATION_JSON_TYPE) )
			// JSON is requested
			return Response.ok(points).build();

		// XML is requested
		final BicicasPointList list = new BicicasPointList();
		list.bicicasPoints = points;
		
		String xml = JAXBUtils.marshal(list);
		try {
			xml = XSLTUtils.applyXSLT(xml, Paths.XSLT.BICICAS_HGEO);
		} catch (XSLTException e) {
			Log.e(TAG, e, ErrorMessages.XSLT_ERROR, Paths.XSLT.BICICAS_HGEO);
		}
		
		return Response.ok(xml).build();
	}
}
