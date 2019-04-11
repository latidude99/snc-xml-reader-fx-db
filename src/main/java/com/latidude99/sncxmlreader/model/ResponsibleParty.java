package com.latidude99.sncxmlreader.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.dizitart.no2.Document;
import org.dizitart.no2.mapper.Mappable;
import org.dizitart.no2.mapper.NitriteMapper;

@XmlRootElement(name = "ResponsibleParty")
@XmlType(propOrder={"organisationName", "contactInfo"})
public class ResponsibleParty implements Serializable, Mappable{
	private static final long serialVersionUID = -8476002317698205847L;

	String organisationName;
	ContactInfo contactInfo;
	
	@Override
	public Document write(NitriteMapper mapper) {
	    Document document = new Document();
	    document.put("organisationName", getOrganisationName());
	    document.put("contactInfo", getContactInfo());
	     
	    return document;
	}

	@Override
	public void read(NitriteMapper mapper, Document document) {
	    if (document != null) {
	        setOrganisationName((String) document.get("organisationName"));
	        setContactInfo((ContactInfo) document.get("contactInfo"));
	    }
	}	

	
	public String getOrganisationName() {
		return organisationName;
	}
	@XmlElement(name = "organisationName")
	public void setOrganisationName(String organisationName) {
		this.organisationName = organisationName;
	}
	public ContactInfo getContactInfo() {
		return contactInfo;
	}
	@XmlElement(name = "contactInfo")
	public void setContactInfo(ContactInfo contactInfo) {
		this.contactInfo = contactInfo;
	}
	
	

}
