package com.latidude99.sncxmlreader.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "GeographicLimit")
public class GeographicLimit implements Serializable{
	private static final long serialVersionUID = -6971830828105911563L;

	List<Polygon> polygons;
    
        
    
   
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
