package com.latidude99.sncxmlreader.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.dizitart.no2.Document;
import org.dizitart.no2.mapper.Mappable;
import org.dizitart.no2.mapper.NitriteMapper;

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
