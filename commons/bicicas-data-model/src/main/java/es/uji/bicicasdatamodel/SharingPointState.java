package es.uji.bicicasdatamodel;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SharingPointState {
	
	public Date updated;
	public Boolean online;
	public Integer bicycleAvailable;
	@XmlElementWrapper(name = "ingoing")
	@XmlElement(name = "dock")
	public List<Dock> ingoing;
	@XmlElementWrapper(name = "outgoing")
	@XmlElement(name = "dock")
	public List<Dock> outgoing;
	@XmlElementWrapper(name = "docks")
	@XmlElement(name = "dock")
	public List<Dock> docks;
	
	
}