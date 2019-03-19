package com.latidude99.sncxmlreader.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.dizitart.no2.Document;
import org.dizitart.no2.mapper.Mappable;
import org.dizitart.no2.mapper.NitriteMapper;


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
	    System.out.println("StandardNavigationChart write, Mappable");
	    return document;
	}

	@Override
	public void read(NitriteMapper mapper, Document document) {
	    if (document != null) {
	        setShortName((String) document.get("shortName"));
	        setMetadata((Metadata) document.get("metadata"));
	        System.out.println("StandardNavigationChart read, Mappable");
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






















