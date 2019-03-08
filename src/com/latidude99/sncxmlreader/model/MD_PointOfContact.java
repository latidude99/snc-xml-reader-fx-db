package com.latidude99.sncxmlreader.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MD_PointOfContact")
public class MD_PointOfContact implements Serializable{
	private static final long serialVersionUID = 2598209114132472218L;
	
	ResponsibleParty responsibleParty;
	
	public ResponsibleParty getResponsibleParty() {
		return responsibleParty;
	}
	@XmlElement(name = "ResponsibleParty")
	public void setResponsibleParty(ResponsibleParty responsibleParty) {
		this.responsibleParty = responsibleParty;
	}
    
	

}
