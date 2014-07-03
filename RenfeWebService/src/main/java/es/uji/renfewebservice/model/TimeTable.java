package es.uji.renfewebservice.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class which embed a list of train trips.
 * <b>Civis</b> keyword means a short trip. That is, the same itinerary with less stops.
 * 
 */
@XmlRootElement
public class TimeTable {

	@XmlElement
	private String departureStationCode;

	@XmlElement
	private String arrivalStationCode;

	@XmlElement
	private Calendar departureDateTime;

	@XmlElement
	private Calendar arrivalDateTime;

	@XmlElementWrapper(name = "trips")
	@XmlElement(name = "trip")
	private List<Trip> trips = new ArrayList<Trip>();

	public TimeTable() {
		super();
	}

	public TimeTable(final String departureStationCode, final String arrivalStationCode, final Calendar departureDateTime, final Calendar arrivalDateTime) {
		super();
		this.departureStationCode = departureStationCode;
		this.arrivalStationCode = arrivalStationCode;
		this.departureDateTime = departureDateTime;
		this.arrivalDateTime = arrivalDateTime;
	}

	public void addTrip(final Trip trip) {
		trips.add(trip);
	}

	public Trip getTrip(final int pos) {
		return trips.get(pos);
	}

	public List<Trip> getTrips() {
		return trips;
	}

	public int tripsNum() {
		return trips.size();
	}
}
