package com.latidude99.sncxmlreader.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "ChartStatus")
public class ChartStatus implements Serializable{
	private static final long serialVersionUID = 5262518838434057397L;
	
	String date;
	String value;
	

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
