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
import java.util.ArrayList;
import java.util.List;

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

@XmlRootElement(name = "Paper")
public class Panel implements Serializable, Mappable{
	private static final long serialVersionUID = -833859172140530569L;

	String panelID;
	String panelAreaName;
 	String panelName;
 	String panelScale;
	List<Polygon> polygons;
 	
 	@Override
 	public Document write(NitriteMapper mapper) {
 	    Document document = new Document();
 	    document.put("panelID", getPanelID());
 	    document.put("panelAreaName", getPanelAreaName());
 	    document.put("panelName", getPanelName());
 	    document.put("panelScale", getPanelScale());
		document.put("polygons", getPolygons());
 	     
 	    return document;
 	}

 	@Override
 	public void read(NitriteMapper mapper, Document document) {
 	    if (document != null) {
 	        setPanelID((String) document.get("panelID"));
 	        setPanelAreaName((String) document.get("panelAreaName"));
 	        setPanelName((String) document.get("panelName"));
 	        setPanelScale((String) document.get("panelScale"));
			setPolygons((List<Polygon>) document.get("polygons"));
 	    }
 	}	
 	
 	public Panel() {}
 	 	
	public Panel(String panelID, String panelAreaName, String panelName, String panelScale) {
		this.panelID = panelID;
		this.panelAreaName = panelAreaName;
		this.panelName = panelName;
		this.panelScale = panelScale;
	}


	public String getPanelID() {
		return panelID;
	}
	@XmlElement(name = "PanelID")
	public void setPanelID(String panelID) {
		this.panelID = panelID;
	}
	public String getPanelAreaName() {
		return panelAreaName;
	}
	@XmlElement(name = "PanelAreaName")
	public void setPanelAreaName(String panelAreaName) {
		this.panelAreaName = panelAreaName;
	}
	public String getPanelName() {
		return panelName;
	}
	@XmlElement(name = "PanelName")
	public void setPanelName(String panelName) {
		this.panelName = panelName;
	}
	public String getPanelScale() {
		return panelScale;
	}
	@XmlElement(name = "PanelScale")
	public void setPanelScale(String panelScale) {
		this.panelScale = panelScale;
	}

	public List<Polygon> getPolygons() {
		return polygons;
	}
	@XmlElement(name = "Polygon")
	public void setPolygons(List<Polygon> polygons) {
		this.polygons = polygons;
	}
 	
	public void add(Polygon polygon) {
        if (this.polygons == null) {
            this.polygons = new ArrayList<Polygon>();
        }
        this.polygons.add(polygon);
	}

}
