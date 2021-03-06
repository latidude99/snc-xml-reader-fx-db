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

@XmlRootElement(name = "BaseFileMetadata")
@XmlType(propOrder={"MD_FileIdentifier", "MD_CharacterSet", "MD_PointOfContact", "MD_DateStamp"})
public class BaseFileMetadata implements Serializable, Mappable{
	private static final long serialVersionUID = 9128827583565489660L;

	String mD_FileIdentifier;
	String mD_CharacterSet;
	MD_PointOfContact mD_PointOfContact;
	String mD_DateStamp;
	
	@Override
	public Document write(NitriteMapper mapper) {
	    Document document = new Document();
	    document.put("mD_FileIdentifier", getMD_FileIdentifier());
	    document.put("mD_CharacterSet", getMD_CharacterSet());
	    document.put("mD_PointOfContact", getMD_PointOfContact());
	    document.put("mD_DateStamp", getMD_DateStamp());
	     
	    return document;
	}

	@Override
	public void read(NitriteMapper mapper, Document document) {
	    if (document != null) {
	        setMD_FileIdentifier((String) document.get("mD_FileIdentifier"));
	        setMD_CharacterSet((String) document.get("mD_CharacterSet"));
	        setMD_PointOfContact((MD_PointOfContact) document.get("mD_PointOfContact"));
	        setMD_DateStamp((String) document.get("mD_DateStamp"));
	    }
	}	

	
	public String getMD_FileIdentifier() {
		return mD_FileIdentifier;
	}
	@XmlElement(name = "MD_FileIdentifier")
	public void setMD_FileIdentifier(String mD_FileIdentifier) {
		this.mD_FileIdentifier = mD_FileIdentifier;
	}

	public String getMD_CharacterSet() {
		return mD_CharacterSet;
	}
	@XmlElement(name = "MD_CharacterSet")
	public void setMD_CharacterSet(String mD_CharacterSet) {
		this.mD_CharacterSet = mD_CharacterSet;
	}

	public MD_PointOfContact getMD_PointOfContact() {
		return mD_PointOfContact;
	}
	@XmlElement(name = "MD_PointOfContact")
	public void setMD_PointOfContact(MD_PointOfContact mD_PointOfContact) {
		this.mD_PointOfContact = mD_PointOfContact;
	}

	public String getMD_DateStamp() {
		return mD_DateStamp;
	}

	@XmlElement(name = "MD_DateStamp")
	public void setMD_DateStamp(String mD_DateStamp) {
		this.mD_DateStamp = mD_DateStamp;
	}
    

}
