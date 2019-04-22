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
import java.io.Serializable;

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
