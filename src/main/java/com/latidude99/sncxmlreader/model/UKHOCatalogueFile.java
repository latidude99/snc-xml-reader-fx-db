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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

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
