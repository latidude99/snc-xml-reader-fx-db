package com.latidude99.sncxmlreader.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.dizitart.no2.Document;
import org.dizitart.no2.mapper.Mappable;
import org.dizitart.no2.mapper.NitriteMapper;

@XmlRootElement(name = "GeographicLimit")
public class GeographicLimit implements Serializable, Mappable{
	private static final long serialVersionUID = -6971830828105911563L;

	List<Polygon> polygons;
    
	@Override
	public Document write(NitriteMapper mapper) {
	    Document document = new Document();
	    document.put("polygons", getPolygons());
	     
	    return document;
	}

	@Override
	public void read(NitriteMapper mapper, Document document) {
	    if (document != null) {
	        setPolygons((List<Polygon>) document.get("polygons"));       
	    }
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
