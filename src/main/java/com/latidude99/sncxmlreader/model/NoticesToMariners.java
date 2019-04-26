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
import java.io.Serializable;

/*
 * Used for parsing an XML catalogue file (JAXB).
 * Mapping methods:
 * 		- public Document write(NitriteMapper mapper)
 * 		- public void read(NitriteMapper mapper, Document document)
 * required by Nitrite Database in order to avoid reflection
 * being used when converting objects into Documents and saving,
 * updating or  searching.
 * Speed gain turned out not to be that significant.
 */

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