package com.latidude99.sncxmlreader.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.dizitart.no2.Document;
import org.dizitart.no2.mapper.Mappable;
import org.dizitart.no2.mapper.NitriteMapper;

@XmlRootElement(name = "UKHOCatalogueFile") 
public class UKHOCatalogueFile implements Serializable, Mappable{
	private static final long serialVersionUID = 2961423601086814890L;

	String schemaVersion;
	BaseFileMetadata baseFileMetadata;
	Products products;
	
	@Override
	public Document write(NitriteMapper mapper) {
	    Document document = new Document();
	    document.put("schemaVersion", getSchemaVersion());
	    document.put("baseFileMetadata", getBaseFileMetadata());
	    document.put("products", getProducts());
	     
	    return document;
	}

	@Override
	public void read(NitriteMapper mapper, Document document) {
	    if (document != null) {
	        setSchemaVersion((String) document.get("schemaVersion"));
	        setBaseFileMetadata((BaseFileMetadata) document.get("baseFileMetadata")); 
	        setProducts((Products) document.get("products")); 
	    }
	}	
	
    public String getSchemaVersion() {
		return schemaVersion;
	}

    @XmlAttribute(name = "SchemaVersion")
	public void setSchemaVersion(String schemaVersion) {
		this.schemaVersion = schemaVersion;
	}

	
    public BaseFileMetadata getBaseFileMetadata() {
		return baseFileMetadata;
	}
    
    @XmlElement(name = "BaseFileMetadata")
	public void setBaseFileMetadata(BaseFileMetadata baseFileMetadata) {
		this.baseFileMetadata = baseFileMetadata;
	}

	public Products getProducts() {
		return products;
	}
    
    @XmlElement(name = "Products")
	public void setProducts(Products products) {
		this.products = products;
	}


}
