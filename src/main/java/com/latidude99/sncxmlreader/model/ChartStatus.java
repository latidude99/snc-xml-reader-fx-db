package com.latidude99.sncxmlreader.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

import org.dizitart.no2.Document;
import org.dizitart.no2.mapper.Mappable;
import org.dizitart.no2.mapper.NitriteMapper;

@XmlRootElement(name = "ChartStatus")
public class ChartStatus implements Serializable, Mappable{
	private static final long serialVersionUID = 5262518838434057397L;
	
	String date;
	String value;
	
	@Override
	public Document write(NitriteMapper mapper) {
	    Document document = new Document();
	    document.put("date", getDate());
	    document.put("value", getValue());
	     
	    return document;
	}

	@Override
	public void read(NitriteMapper mapper, Document document) {
	    if (document != null) {
	        setDate((String) document.get("date"));
	        setValue((String) document.get("value"));
	    }
	}	
	

	public String getDate() {
		return date;
	}
	@XmlAttribute(name = "date")
	public void setDate(String date) {
		this.date = date;
	}
	public String getValue() {
		return value;
	}
	@XmlValue
	public void setValue(String value) {
		this.value = value;
	}

	   

}
