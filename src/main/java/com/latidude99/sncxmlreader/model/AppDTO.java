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

import java.io.Serializable;

/*
 * Creates object holding additional info for the app to use when starting up.
 * (eg. if a chart catalogue has been already loaded up into the database)
 */

public class AppDTO implements Serializable, Mappable{
	private static final long serialVersionUID = -4634514619595007673L;
	
	String schemaVersion;
	String textResult;
	String labelLoadedDate;
	String labelInputError;
	String textSearchChart;

	@Override
	public Document write(NitriteMapper mapper) {
	    Document document = new Document();
	    document.put("schemaVersion", getSchemaVersion());
	    document.put("textResult", getTextResult());
	    document.put("labelLoadedDate", getLabelLoadedDate());
	    document.put("labelInputError", getLabelInputError());
	    document.put("textSearchChart", getTextSearchChart());

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

	
	
	
	

}





















