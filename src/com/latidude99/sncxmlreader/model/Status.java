package com.latidude99.sncxmlreader.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Status")
public class Status implements Serializable{
	private static final long serialVersionUID = 6940183421802603050L;

	ChartStatus chartStatus;
	
	public ChartStatus getChartStatus() {
		return chartStatus;
	}
	@XmlElement(name = "ChartStatus")
	public void setChartStatus(ChartStatus chartStatus) {
		this.chartStatus = chartStatus;
	}

	  

}
