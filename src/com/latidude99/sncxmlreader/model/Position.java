package com.latidude99.sncxmlreader.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Position")
public class Position implements Serializable{
	private static final long serialVersionUID = 7231617995045145333L;

	String latitude;
	String	longitude;
	
	public Position() {}
	public Position(String lat, String lon ) {
		this.latitude = lat;
		this.longitude = lon;
	}
	
	public String getLatitude() {
		return latitude;
	}
	
	@XmlAttribute(name = "latitude")
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	
	@XmlAttribute(name = "longitude")
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	

}
