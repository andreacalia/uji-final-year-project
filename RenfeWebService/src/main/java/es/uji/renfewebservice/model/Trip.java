package es.uji.renfewebservice.model;

import java.util.Calendar;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class which embed information about the trip. <b>Civis</b> keyword means a
 * short trip. That is, the same itinerary with less stops.
 * 
 */
@XmlRootElement
public class Trip {

	@XmlElement
	private String line;

	@XmlElement
	private Calendar departureDateTime;

	@XmlElement
	private Calendar arrivalDateTime;

	@XmlElement
	private String tripTime;

	@XmlElement
	private boolean civis;
	
	@XmlElement
	private String name;

	public Trip() {
		super();
	}

	public Trip(String line, Calendar departureDateTime, Calendar arrivalDateTime, String tripTime, boolean civis) {
		super();
		this.line = line;
		this.departureDateTime = departureDateTime;
		this.arrivalDateTime = arrivalDateTime;
		this.tripTime = tripTime;
		this.civis = civis;
		this.name = "Train trip from Castellón de la Plana to Valencia Nord";
	}

	public String getName() {
		return name;
	}

	public boolean isCivis() {
		return civis;
	}

	public String getLine() {
		return line;
	}

	public Calendar getDepartureDateTime() {
		return departureDateTime;
	}

	public Calendar getArrivalDateTime() {
		return arrivalDateTime;
	}

	public String getTripTime() {
		return tripTime;
	}

}
