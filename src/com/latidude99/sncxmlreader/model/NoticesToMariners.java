package com.latidude99.sncxmlreader.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.dizitart.no2.Document;
import org.dizitart.no2.mapper.Mappable;
import org.dizitart.no2.mapper.NitriteMapper;

@XmlRootElement(name = "NoticesToMariners")
//@XmlAccessorType(XmlAccessType.FIELD)
public class NoticesToMariners implements Serializable, Mappable{
  	private static final long serialVersionUID = 1608943645224806165L;

	@XmlElement(name = "Year")
    private String year;
    
    @XmlElement(name = "Week")
    private String week;
    
    @XmlElement(name = "Number")
    private int number;
    
    @XmlElement(name = "Type")
    private String type;
    
    @Override
    public Document write(NitriteMapper mapper) {
        Document document = new Document();
        document.put("year", getYearMapper());
        document.put("week", getWeekMapper());
        document.put("number", getNumberMapper());
        document.put("type", getTypeMapper());
         
        return document;
    }

    @Override
    public void read(NitriteMapper mapper, Document document) {
        if (document != null) {
            setYearMapper((String) document.get("year"));
            setWeekMapper((String) document.get("week"));
            setNumberMapper((int) document.get("number"));
            setTypeMapper((String) document.get("type"));
        }
    }	
    
    public NoticesToMariners(){}
    
    public NoticesToMariners(String year, String week,
                   int number, String type) {
        this.year = year;
        this.week = week;
        this.number = number;
        this.type = type;
    }
    @Override
    public String toString() {
        return "           Year: " + year + ", " + "Week: " + week + ", " + "Number: " + number + ", " +"Type: " + type;
    }

	public String getYearMapper() {
		return year;
	}
	public void setYearMapper(String year) {
		this.year = year;
	}
	public String getWeekMapper() {
		return week;
	}
	public void setWeekMapper(String week) {
		this.week = week;
	}
	public int getNumberMapper() {
		return number;
	}
	public void setNumberMapper(int number) {
		this.number = number;
	}
	public String getTypeMapper() {
		return type;
	}
	public void setTypeMapper(String type) {
		this.type = type;
	}
    
}