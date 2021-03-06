/**Copyright (C) 2017  Piotr Czapik.
 * @author Piotr Czapik
 * @version 2.3
 *
 *  This file is part of SncXmlReaderFXDB.
 *  SncXmlReaderFXDB is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  SncXmlReaderFXDB is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with SncXmlReaderFXDB.  If not, see <http://www.gnu.org/licenses/>
 *  or write to: latidude99@gmail.com
 */

package com.latidude99.sncxmlreader.model;


import org.dizitart.no2.Document;
import org.dizitart.no2.mapper.Mappable;
import org.dizitart.no2.mapper.NitriteMapper;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/*
 * Used for parsing an XML catalogue file (JAXB).
 * Mapping methods:
 * 		- public Document write(NitriteMapper mapper)
 * 		- public void read(NitriteMapper mapper, Document document)
 * required by Nitrite Database in order to avoid reflection
 * being used when converting objects into Documents and saving,
 * updating or  searching.
 * Speed gain turned out not to be that significant.
 */

@XmlRootElement(name = "address")
@XmlType(propOrder={"deliveryPoint", "city", "administrativeArea", "postalCode", "country", "electronicMailAddress"})
public class Address implements Serializable, Mappable{
	private static final long serialVersionUID = 1706674944343606484L;

	String deliveryPoint;
	String city;
	String administrativeArea;
	String postalCode;
	String country;
	String electronicMailAddress;
	
	@Override
	public Document write(NitriteMapper mapper) {
	    Document document = new Document();
	    document.put("deliveryPoint", getDeliveryPoint());
	    document.put("city", getCity());
	    document.put("administrativeArea", getAdministrativeArea());
	    document.put("postalCode", getPostalCode());
	    document.put("country", getCountry());
	    document.put("electronicMailAddress", getElectronicMailAddress());
	     
	    return document;
	}

	@Override
	public void read(NitriteMapper mapper, Document document) {
	    if (document != null) {
	        setDeliveryPoint((String) document.get("deliveryPoint"));
	        setCity((String) document.get("city"));
	        setAdministrativeArea((String) document.get("administrativeArea"));
	        setPostalCode((String) document.get("postalCode"));
	        setCountry((String) document.get("country"));
	        setElectronicMailAddress((String) document.get("electronicMailAddress"));     
	    }
	}	
	
	public String getDeliveryPoint() {
		return deliveryPoint;
	}
	@XmlElement(name = "deliveryPoint")
	public void setDeliveryPoint(String deliveryPoint) {
		this.deliveryPoint = deliveryPoint;
	}
	public String getCity() {
		return city;
	}
	@XmlElement(name = "city")
	public void setCity(String city) {
		this.city = city;
	}
	public String getAdministrativeArea() {
		return administrativeArea;
	}
	@XmlElement(name = "administrativeArea")
	public void setAdministrativeArea(String administrativeArea) {
		this.administrativeArea = administrativeArea;
	}
	public String getPostalCode() {
		return postalCode;
	}
	@XmlElement(name = "postalCode")
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getCountry() {
		return country;
	}
	@XmlElement(name = "country")
	public void setCountry(String country) {
		this.country = country;
	}
	public String getElectronicMailAddress() {
		return electronicMailAddress;
	}
	@XmlElement(name = "electronicMailAddress")
	public void setElectronicMailAddress(String electronicMailAddress) {
		this.electronicMailAddress = electronicMailAddress;
	}
	@Override
	public String toString() {
		return  "\n         Delivery Point: " + deliveryPoint + 
				"\n         City: " + city + 
				"\n         Administrative Area: " + administrativeArea + 
				"\n         Postal Code: " + postalCode + 
				"\n         Country: " + country;
				
	}
	
	
	
	

}
