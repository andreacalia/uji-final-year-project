package es.uji.bicicasdatamodel;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import es.uji.geotec.commondatamodel.GeoJSONPoint;

/**
 * Model class representing a sharing point station of Castellón's bike rental service (Bicicas)
 *
 */
@XmlRootElement
public class BicicasPoint {

//	// If it is private, the jackson marshaller do not show this attribute
//	@SuppressWarnings("unused")
//	private ObjectId _id;
	public Integer code;
	public String name;
	public Location location;
	public Integer bicycleNumber;
	public SharingPointState currentState;
	@XmlTransient
	public GeoJSONPoint geoJson;
	
}
