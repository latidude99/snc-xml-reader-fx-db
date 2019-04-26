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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
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

@XmlRootElement(name = "Position")
public class Position implements Serializable, Mappable{
	private static final long serialVersionUID = 7231617995045145333L;

	String latitude;
	String	longitude;
	
	@Override
	public Document write(NitriteMapper mapper) {
	    Document document = new Document();
	    document.put("latitude", getLatitude());
	    document.put("longitude", getLongitude());
	     
	    return document;
	}

	@Override
	public void read(NitriteMapper mapper, Document document) {
	    if (document != null) {
	        setLatitude((String) document.get("latitude"));
	        setLongitude((String) document.get("longitude"));
	    }
	}	
	
	public Position() {}
	public Position(String lat, String lon ) {
		this.latitude = lat;
		this.longitude = lon;
	}
	
	public String getLatitude() {
		return latitude;
	}
	
	@XmlAttribute(name = "latitude")
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	
	@XmlAttribute(name = "longitude")
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	

}
