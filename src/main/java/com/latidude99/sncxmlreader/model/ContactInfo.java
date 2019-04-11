package com.latidude99.sncxmlreader.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.dizitart.no2.Document;
import org.dizitart.no2.mapper.Mappable;
import org.dizitart.no2.mapper.NitriteMapper;

@XmlRootElement(name = "contactInfo")
@XmlType(propOrder={"fax", "phone", "address"})
public class ContactInfo implements Serializable, Mappable{
	private static final long serialVersionUID = -2207800331547888470L;

	String fax;
	String phone;
	Address address;
	
	@Override
	public Document write(NitriteMapper mapper) {
	    Document document = new Document();
	    document.put("fax", getFax());
	    document.put("phone", getPhone());
	    document.put("address", getAddress());
	     
	    return document;
	}

	@Override
	public void read(NitriteMapper mapper, Document document) {
	    if (document != null) {
	        setFax((String) document.get("fax"));
	        setPhone((String) document.get("phone"));
	        setAddress((Address) document.get("address"));
	    }
	}	
	public String getFax() {
		return fax;
	}
	@XmlElement(name = "fax")
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getPhone() {
		return phone;
	}
	@XmlElement(name = "phone")
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Address getAddress() {
		return address;
	}
	@XmlElement(name = "address")
	public void setAddress(Address address) {
		this.address = address;
	}
	
	
	

}
