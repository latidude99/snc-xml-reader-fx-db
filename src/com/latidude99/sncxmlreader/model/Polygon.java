package com.latidude99.sncxmlreader.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.dizitart.no2.Document;
import org.dizitart.no2.mapper.Mappable;
import org.dizitart.no2.mapper.NitriteMapper;

@XmlRootElement(name = "Polygon")
public class Polygon implements Serializable, Mappable{
	private static final long serialVersionUID = 7552335406525881110L;

	List<Position> positions;
	
	@Override
	public Document write(NitriteMapper mapper) {
	    Document document = new Document();
	    document.put("positions", getPositions());
	     
	    return document;
	}

	@Override
	public void read(NitriteMapper mapper, Document document) {
	    if (document != null) {
	        setPositions((List<Position>) document.get("positions"));       
	    }
	}	

	public List<Position> getPositions() {
		return positions;
	}
	
	@XmlElement(name = "Position")
	public void setPositions(List<Position> position) {
		this.positions = position;
	}
	
	public void add(Position position) {
        if (this.positions == null) {
            this.positions = new ArrayList<Position>();
        }
        this.positions.add(position);
	}
	
}
