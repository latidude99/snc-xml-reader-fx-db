package com.latidude99.sncxmlreader.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.dizitart.no2.Document;
import org.dizitart.no2.mapper.Mappable;
import org.dizitart.no2.mapper.NitriteMapper;

@XmlRootElement(name = "MD_PointOfContact")
public class MD_PointOfContact implements Serializable, Mappable{
	private static final long serialVersionUID = 2598209114132472218L;
	
	ResponsibleParty responsibleParty;
	
	@Override
	public Document write(NitriteMapper mapper) {
	    Document document = new Document();
	    document.put("responsibleParty", getResponsibleParty());
	     
	    return document;
	}

	@Override
	public void read(NitriteMapper mapper, Document document) {
	    if (document != null) {
	        setResponsibleParty((ResponsibleParty) document.get("responsibleParty"));       
	    }
	}	
	
	public ResponsibleParty getResponsibleParty() {
		return responsibleParty;
	}
	@XmlElement(name = "ResponsibleParty")
	public void setResponsibleParty(ResponsibleParty responsibleParty) {
		this.responsibleParty = responsibleParty;
	}
    
	

}
