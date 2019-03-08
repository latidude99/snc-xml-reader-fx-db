package com.latidude99.sncxmlreader.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "contactInfo")
@XmlType(propOrder={"fax", "phone", "address"})
public class ContactInfo implements Serializable{
	private static final long serialVersionUID = -2207800331547888470L;

	String fax;
	String phone;
	Address address;
	
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
