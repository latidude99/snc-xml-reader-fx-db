package com.latidude99.sncxmlreader.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.dizitart.no2.Document;
import org.dizitart.no2.mapper.Mappable;
import org.dizitart.no2.mapper.NitriteMapper;

@XmlRootElement(name = "Status")
public class Status implements Serializable, Mappable{
	private static final long serialVersionUID = 6940183421802603050L;

	ChartStatus chartStatus;
	
	@Override
	public Document write(NitriteMapper mapper) {
	    Document document = new Document();
	    document.put("chartStatus", getChartStatus());
	     
	    return document;
	}

	@Override
	public void read(NitriteMapper mapper, Document document) {
	    if (document != null) {
	        setChartStatus((ChartStatus) document.get("chartStatus"));       
	    }
	}	

	
	public ChartStatus getChartStatus() {
		return chartStatus;
	}
	@XmlElement(name = "ChartStatus")
	public void setChartStatus(ChartStatus chartStatus) {
		this.chartStatus = chartStatus;
	}

	  

}
