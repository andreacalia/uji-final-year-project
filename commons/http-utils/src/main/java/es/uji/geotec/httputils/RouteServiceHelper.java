package es.uji.geotec.httputils;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.glassfish.jersey.client.ClientConfig;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import es.uji.geotec.commondatamodel.GeoJSONRoute;
import es.uji.geotec.commonutils.Log;
import es.uji.geotec.httputils.exception.RouteServiceException;
import es.uji.geotec.httputils.message.ErrorMessages;

public class RouteServiceHelper {

	private static final String TAG = RouteServiceHelper.class.getName();
	
	private final static String YOURNAV_TARGET = "http://www.yournavigation.org";
	private final static String YOURNAV_PATH = "/api/1.0/gosmore.php";
	private final static String KML_XPATH_DISTANCE = "/kml/Document/distance";
	private final static String KML_XPATH_TRAVELTIME= "/kml/Document/traveltime";
	private final static String KML_XPATH_COORDINATES = "/kml/Document/Folder/Placemark/LineString/coordinates";
	
	public static GeoJSONRoute getRoute(
			final String flon, final String flat,
			final String tlon, final String tlat,
			final String v) throws RouteServiceException {

		// Web request
		final Client client = ClientBuilder.newClient(new ClientConfig());
		final WebTarget target = client.target(YOURNAV_TARGET)
				.path(YOURNAV_PATH)
				.queryParam("format", "kml")
				.queryParam("flat", flat)
				.queryParam("flon", flon)
				.queryParam("tlat", tlat)
				.queryParam("tlon", tlon)
				.queryParam("v", v)
				.queryParam("fast", 0)
				.queryParam("layer", "mapnik");
		
		final String xmlResponse = WebExecutor.syncGetRequest(target, MediaType.APPLICATION_XML_TYPE);
		
		client.close();
		
		// If there will be an error
		Throwable error = null;
		
		try {
			// Parsing the KML
			final DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			final Document xmlDoc = builder.parse(new ByteArrayInputStream(xmlResponse.getBytes()));
			final XPath xpath = XPathFactory.newInstance().newXPath();
			
			final String distance = (String) xpath.evaluate(KML_XPATH_DISTANCE, xmlDoc, XPathConstants.STRING);
			final String travelTime = (String) xpath.evaluate(KML_XPATH_TRAVELTIME, xmlDoc, XPathConstants.STRING);
			final String[] coordinatesString = ((String) xpath.evaluate(KML_XPATH_COORDINATES, xmlDoc, XPathConstants.STRING))
					.trim()
					.replaceAll("\n", " ")
					.split(" "); // {"lon,lat", "lon,lat", ...}
			
			// Building GeoJSON object
			final GeoJSONRoute route = new GeoJSONRoute();
			route.properties.distance = Double.parseDouble(distance);
			route.properties.travelTime = Double.parseDouble(travelTime);
			
			for(int i = 0; i < coordinatesString.length; i++) {
				final String[] stringCoord = coordinatesString[i].split(",");
				final double[] doubleCoord = new double[]{
						Double.parseDouble(stringCoord[0]), // lon
						Double.parseDouble(stringCoord[1]) // lat
				};
				route.geometry.coordinates.add(doubleCoord);
			}
			
			return route;
			
		} catch (NumberFormatException e) {
			Log.e(TAG, e, ErrorMessages.ROUTE_SERVICE_COORDINATE_ERROR);
			error = e;
		} catch (ParserConfigurationException e) {
			Log.e(TAG, e, ErrorMessages.ROUTE_SERVICE_PARSER_CONFIGURATION_ERROR);
			error = e;
		} catch (SAXException e) {
			Log.e(TAG, e, ErrorMessages.ROUTE_SERVICE_SAX_ERROR);
			error = e;
		} catch (IOException e) {
			Log.e(TAG, e, ErrorMessages.IO_ERROR);
		} catch (XPathExpressionException e) {
			Log.e(TAG, e, ErrorMessages.ROUTE_SERVICE_XPATH_ERROR);
			error = e;
		}
		
		throw new RouteServiceException(error);
	}
}
