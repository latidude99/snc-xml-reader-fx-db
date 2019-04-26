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

@XmlRootElement(name = "StandardNavigationChart")
@XmlType(propOrder={"shortName","metadata"}) 
public class StandardNavigationChart implements Serializable, Mappable{
	private static final long serialVersionUID = -2504886200239974764L;
	
	String shortName;
	Metadata metadata;
	
	 @Override
	 public Document write(NitriteMapper mapper) {
	    Document document = new Document();
	    document.put("shortName", getShortName());
	    document.put("metadata", getMetadata());
	    return document;
	}

	@Override
	public void read(NitriteMapper mapper, Document document) {
	    if (document != null) {
	        setShortName((String) document.get("shortName"));
	        setMetadata((Metadata) document.get("metadata"));
	    }
	}
 	
    public String getShortName() {
		return shortName;
	}
    @XmlElement(name = "ShortName")
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public Metadata getMetadata() {
		return metadata;
	}
    @XmlElement(name = "Metadata")
	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}
       

}






















