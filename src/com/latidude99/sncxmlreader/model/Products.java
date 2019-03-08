package com.latidude99.sncxmlreader.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "StandardNavigationChart")
public class Products implements Serializable{
	private static final long serialVersionUID = -3088209017453273656L;

	Paper paper;

	public Paper getPaper() {
		return paper;
	}
	
	@XmlElement(name = "Paper")
	public void setPaper(Paper paper) {
		this.paper = paper;
	}
 	
    

}
