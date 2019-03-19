package com.latidude99.sncxmlreader.model;

import java.io.Serializable;

import org.dizitart.no2.Document;
import org.dizitart.no2.mapper.Mappable;
import org.dizitart.no2.mapper.NitriteMapper;

public class AppDTO implements Serializable, Mappable{
	private static final long serialVersionUID = -4634514619595007673L;
	
	String schemaVersion;
	String textResult;
	String labelLoadedDate;
	String labelInputError;
	String textSearchChart;
	String textSlot1;
	String textSlot2;
	String textSlot3;
	String textSlot4;
	String textSlot5;
	
	@Override
	public Document write(NitriteMapper mapper) {
	    Document document = new Document();
	    document.put("schemaVersion", getSchemaVersion());
	    document.put("textResult", getTextResult());
	    document.put("labelLoadedDate", getLabelLoadedDate());
	    document.put("labelInputError", getLabelInputError());
	    document.put("textSearchChart", getTextSearchChart());
	    document.put("textSlot1", getTextSlot1());
	    document.put("textSlot2", getTextSlot2());
	    document.put("textSlot3", getTextSlot3());
	    document.put("textSlot4", getTextSlot4());
	    document.put("textSlot5", getTextSlot5());
	     
	    return document;
	}

	@Override
	public void read(NitriteMapper mapper, Document document) {
	    if (document != null) {
	        setSchemaVersion((String) document.get("schemaVersion"));
	        setTextResult((String) document.get("textResult"));
	        setLabelLoadedDate((String) document.get("labelLoadedDate"));
	        setLabelInputError((String) document.get("labelInputError"));
	        setTextSearchChart((String) document.get("textSearchChart"));
	        setTextSlot1((String) document.get("textSlot1"));
	        setTextSlot2((String) document.get("textSlot2"));
	        setTextSlot3((String) document.get("textSlot3"));
	        setTextSlot4((String) document.get("textSlot4"));
	        setTextSlot5((String) document.get("textSlot5"));
	    }
	}	
	
	public String getSchemaVersion() {
		return schemaVersion;
	}
	public void setSchemaVersion(String schemaVersion) {
		this.schemaVersion = schemaVersion;
	}
	public String getTextResult() {
		return textResult;
	}
	public void setTextResult(String textResult) {
		this.textResult = textResult;
	}
	public String getLabelLoadedDate() {
		return labelLoadedDate;
	}
	public void setLabelLoadedDate(String labelLoadedDate) {
		this.labelLoadedDate = labelLoadedDate;
	}
	public String getLabelInputError() {
		return labelInputError;
	}
	public void setLabelInputError(String labelInputError) {
		this.labelInputError = labelInputError;
	}
	public String getTextSearchChart() {
		return textSearchChart;
	}
	public void setTextSearchChart(String textSearchChart) {
		this.textSearchChart = textSearchChart;
	}
	public String getTextSlot1() {
		return textSlot1;
	}
	public void setTextSlot1(String textSlot1) {
		this.textSlot1 = textSlot1;
	}
	public String getTextSlot2() {
		return textSlot2;
	}
	public void setTextSlot2(String textSlot2) {
		this.textSlot2 = textSlot2;
	}
	public String getTextSlot3() {
		return textSlot3;
	}
	public void setTextSlot3(String textSlot3) {
		this.textSlot3 = textSlot3;
	}
	public String getTextSlot4() {
		return textSlot4;
	}
	public void setTextSlot4(String textSlot4) {
		this.textSlot4 = textSlot4;
	}
	public String getTextSlot5() {
		return textSlot5;
	}
	public void setTextSlot5(String textSlot5) {
		this.textSlot5 = textSlot5;
	}
	
	
	
	

}





















