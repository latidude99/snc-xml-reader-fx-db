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
