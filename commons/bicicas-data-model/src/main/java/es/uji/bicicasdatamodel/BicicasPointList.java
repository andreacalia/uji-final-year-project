package es.uji.bicicasdatamodel;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="bicicasPoints")
public class BicicasPointList {

	@XmlElement(name = "bicicasPoint")
	public List<BicicasPoint> bicicasPoints;
	
}
