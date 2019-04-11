package com.latidude99.sncxmlreader.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.dizitart.no2.Document;
import org.dizitart.no2.mapper.Mappable;
import org.dizitart.no2.mapper.NitriteMapper;

@XmlRootElement(name = "Paper")
public class Scale implements Serializable, Mappable{
	private static final long serialVersionUID = -2555393992532933736L;

	StandardNavigationChart standardNavigationChart;
	
	@Override
	public Document write(NitriteMapper mapper) {
	    Document document = new Document();
	    document.put("standardNavigationChart", getStandardNavigationChart());
	     
	    return document;
	}

	@Override
	public void read(NitriteMapper mapper, Document document) {
	    if (document != null) {
	        setStandardNavigationChart((StandardNavigationChart) document.get("standardNavigationChart"));       
	    }
	}	


	public StandardNavigationChart getStandardNavigationChart() {
		return standardNavigationChart;
	}
	
	@XmlElement(name = "StandardNavigationChart")
	public void setStandardNavigationChart(StandardNavigationChart standardNavigationChart) {
		this.standardNavigationChart = standardNavigationChart;
	}
 	
   

}
