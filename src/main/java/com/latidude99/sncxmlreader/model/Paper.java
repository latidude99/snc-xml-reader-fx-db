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
public class Paper implements Serializable, Mappable{
	private static final long serialVersionUID = -6240733442840474528L;

	List<StandardNavigationChart> charts;
	
	@Override
	public Document write(NitriteMapper mapper) {
	    Document document = new Document();
	    document.put("charts", getCharts());
	     
	    return document;
	}

	@Override
	public void read(NitriteMapper mapper, Document document) {
	    if (document != null) {
	        setCharts((List<StandardNavigationChart>) document.get("charts"));       
	    }
	}	

	public List<StandardNavigationChart> getCharts() {
		return charts;
	}

	@XmlElement(name = "StandardNavigationChart")
	public void setCharts(List<StandardNavigationChart> charts) {
		this.charts = charts;
	}

	public void add(StandardNavigationChart chart) {
        if (this.charts == null) {
            this.charts = new ArrayList<StandardNavigationChart>();
        }
        this.charts.add(chart);
    }
 	
   

}
