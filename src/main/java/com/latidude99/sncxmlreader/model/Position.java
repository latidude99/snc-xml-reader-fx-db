package com.latidude99.sncxmlreader.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.dizitart.no2.Document;
import org.dizitart.no2.mapper.Mappable;
import org.dizitart.no2.mapper.NitriteMapper;

@XmlRootElement(name = "Position")
public class Position implements Serializable, Mappable{
	private static final long serialVersionUID = 7231617995045145333L;

	String latitude;
	String	longitude;
	
	@Override
	public Document write(NitriteMapper mapper) {
	    Document document = new Document();
	    document.put("latitude", getLatitude());
	    document.put("longitude", getLongitude());
	     
	    return document;
	}

	@Override
	public void read(NitriteMapper mapper, Document document) {
	    if (document != null) {
	        setLatitude((String) document.get("latitude"));
	        setLongitude((String) document.get("longitude"));
	    }
	}	
	
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
