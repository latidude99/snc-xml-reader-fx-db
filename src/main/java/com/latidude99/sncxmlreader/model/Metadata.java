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
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "Metadata")
@XmlType(propOrder={"datasetTitle", "scale", "geographicLimit", "catalogueNumber", 
					"status", "chartID", "chartNumber", "chartInternationalNumber", 
					"chartNewEditionDate", "panels", "notices", "editionNumber", 
					"lastNMNumber", "lastNMDate", "publicationDate"})
public class Metadata implements Serializable, Mappable{
	private static final long serialVersionUID = 1314505904301660392L;

	String datasetTitle;
	String scale;
	GeographicLimit geographicLimit;
	String catalogueNumber;
	Status status;
	String chartID;
	String chartNumber;
	String chartInternationalNumber;
	String chartNewEditionDate;
	List<Panel> panels;
	List<NoticesToMariners> notices;
	String editionNumber;
	String lastNMNumber;
	String lastNMDate;
	String publicationDate;
    
	 @Override
	    public Document write(NitriteMapper mapper) {
	        Document document = new Document();
	        document.put("datasetTitle", getDatasetTitle());
	        document.put("scale", getScale());
	        document.put("geographicLimit", getGeographicLimit());
	        document.put("catalogueNumber", getCatalogueNumber());
	        document.put("status", getStatus());
	        document.put("chartID", getChartID());
	        document.put("chartNumber", getChartNumber());
	        document.put("chartInternationalNumber", getChartInternationalNumber());
	        document.put("chartNewEditionDate", getChartNewEditionDate());
	        document.put("panels", getPanels());
	        document.put("notices", getNotices());
	        document.put("editionNumber", getEditionNumber());
	        document.put("lastNMNumber", getLastNMNumber());
	        document.put("lastNMDate", getLastNMDate());
	        document.put("publicationDate", getPublicationDate());
	        
	        
	        return document;
	    }

	    @Override
	    public void read(NitriteMapper mapper, Document document) {
	        if (document != null) {
	            setDatasetTitle((String) document.get("datasetTitle"));
	            setScale((String) document.get("scale"));
	            setGeographicLimit((GeographicLimit) document.get("geographicLimit"));
	            setCatalogueNumber((String) document.get("catalogueNumber"));
	            setStatus((Status) document.get("status"));
	            setChartID((String) document.get("chartID"));
	            setChartNumber((String) document.get("chartNumber"));
	            setChartInternationalNumber((String) document.get("chartInternationalNumber"));
	            setChartNewEditionDate((String) document.get("chartNewEditionDate"));
	            setPanels((List<Panel>) document.get("panels"));
	            setNotices((List<NoticesToMariners>) document.get("notices"));
	            setEditionNumber((String) document.get("editionNumber"));
	            setLastNMNumber((String) document.get("lastNMNumber"));
	            setLastNMDate((String) document.get("lastNMDate"));
	            setPublicationDate((String) document.get("publicationDate"));
	        }
	    }
        
    public String getDatasetTitle() {
		return datasetTitle;
	}
    @XmlElement(name = "DatasetTitle")
	public void setDatasetTitle(String datasetTitle) {
		this.datasetTitle = datasetTitle;
	}

	public String getScale() {
		return scale;
	}
    
    @XmlElement(name = "Scale")
	public void setScale(String scale) {
		this.scale = scale;
	}
    public GeographicLimit getGeographicLimit() {
		return geographicLimit;
	}
	@XmlElement(name = "GeographicLimit")
	public void setGeographicLimit(GeographicLimit geographicLimit) {
		this.geographicLimit = geographicLimit;
	}
	public List<NoticesToMariners> getNotices() {
        return notices;
    }
    @XmlElement(name = "NoticesToMariners")
    public void setNotices(List<NoticesToMariners> notices) {
        this.notices = notices;
    }
    public void add(NoticesToMariners notice) {
        if (this.notices == null) {
            this.notices = new ArrayList<NoticesToMariners>();
        }
        this.notices.add(notice);
    }
	public String getCatalogueNumber() {
		return catalogueNumber;
	}
	@XmlElement(name = "CatalogueNumber")
	public void setCatalogueNumber(String catalogueNumber) {
		this.catalogueNumber = catalogueNumber;
	}
	public Status getStatus() {
		return status;
	}
	@XmlElement(name = "Status")
	public void setStatus(Status status) {
		this.status = status;
	}
	public String getChartID() {
		return chartID;
	}
	@XmlElement(name = "ChartID")
	public void setChartID(String chartID) {
		this.chartID = chartID;
	}
	public String getChartNumber() {
		return chartNumber;
	}
	@XmlElement(name = "ChartNumber")
	public void setChartNumber(String chartNumber) {
		this.chartNumber = chartNumber;
	}
	public String getChartNewEditionDate() {
		return chartNewEditionDate;
	}
	@XmlElement(name = "ChartNewEditionDate")
	public void setChartNewEditionDate(String chartNewEditionDate) {
		this.chartNewEditionDate = chartNewEditionDate;
	}
	public List<Panel> getPanels() {
		return panels;
	}
	@XmlElement(name = "Panel")
	public void setPanels(List<Panel> panels) {
		this.panels = panels;
	}
	
	public void add(Panel panel) {
        if (this.panels == null) {
            this.panels = new ArrayList<Panel>();
        }
        this.panels.add(panel);
    }
	public String getEditionNumber() {
		return editionNumber;
	}
	@XmlElement(name = "EditionNumber")
	public void setEditionNumber(String editionNumber) {
		this.editionNumber = editionNumber;
	}
	public String getLastNMNumber() {
		return lastNMNumber;
	}
	@XmlElement(name = "LastNMNumber")
	public void setLastNMNumber(String lastNMNumber) {
		this.lastNMNumber = lastNMNumber;
	}
	public String getLastNMDate() {
		return lastNMDate;
	}
	@XmlElement(name = "LastNMDate")
	public void setLastNMDate(String lastNMDate) {
		this.lastNMDate = lastNMDate;
	}
	public String getPublicationDate() {
		return publicationDate;
	}
	@XmlElement(name = "PublicationDate")
	public void setPublicationDate(String publicationDate) {
		this.publicationDate = publicationDate;
	}
	public String getChartInternationalNumber() {
		return chartInternationalNumber;
	}
	@XmlElement(name = "ChartInternationalNumber")
	public void setChartInternationalNumber(String chartInternationalNumber) {
		this.chartInternationalNumber = chartInternationalNumber;
	}
    

 
}
