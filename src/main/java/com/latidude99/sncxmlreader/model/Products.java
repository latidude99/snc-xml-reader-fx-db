package com.latidude99.sncxmlreader.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.dizitart.no2.Document;
import org.dizitart.no2.mapper.Mappable;
import org.dizitart.no2.mapper.NitriteMapper;

@XmlRootElement(name = "StandardNavigationChart")
public class Products implements Serializable, Mappable{
	private static final long serialVersionUID = -3088209017453273656L;

	Paper paper;
	
	@Override
	public Document write(NitriteMapper mapper) {
	    Document document = new Document();
	    document.put("paper", getPaper());
	     
	    return document;
	}

	@Override
	public void read(NitriteMapper mapper, Document document) {
	    if (document != null) {
	        setPaper((Paper) document.get("paper"));       
	    }
	}	

	public Paper getPaper() {
		return paper;
	}
	
	@XmlElement(name = "Paper")
	public void setPaper(Paper paper) {
		this.paper = paper;
	}
 	
    

}
