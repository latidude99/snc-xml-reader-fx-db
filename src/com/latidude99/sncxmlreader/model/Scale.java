package com.latidude99.sncxmlreader.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Paper")
public class Scale implements Serializable{
	private static final long serialVersionUID = -2555393992532933736L;

	StandardNavigationChart standardNavigationChart;

	public StandardNavigationChart getStandardNavigationChart() {
		return standardNavigationChart;
	}
	
	@XmlElement(name = "StandardNavigationChart")
	public void setStandardNavigationChart(StandardNavigationChart standardNavigationChart) {
		this.standardNavigationChart = standardNavigationChart;
	}
 	
   

}
