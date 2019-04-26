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

package com.latidude99.sncxmlreader.map;

/*
 * Composes an HTML document that consist of a the head, body and script parts.
 * The head (including the style) and the body with the map cancas elemet are
 * the same for all generated documents.
 * Scripts are generated depending on specific charts to be displayed. It contains
 * also Google Maps Javascript API (read from config.properties file).
 */
public class HTMLContent {
	private String script;
	private String htmlBlockStart;
	private String htmlBlockEnd;
	
	public HTMLContent(ScriptBlock scriptBlock) {
		this.script = scriptBlock.getScript();
	}
	
	public String getContent() {
		setHtmlBlocks();
		String contentAll = htmlBlockStart + script + htmlBlockEnd;
		return contentAll;
	}
	
	
	private void setHtmlBlocks(){
		htmlBlockStart = "<!DOCTYPE html>\r\n" + 
			"<html>\r\n" + 
			"  <head>\r\n" + 
			"    <meta name=\"viewport\" content=\"initial-scale=1.0, user-scalable=no\">\r\n" + 
			"    <meta charset=\"utf-8\">\r\n" + 
			"    <title>Chart Display</title>\r\n" + 
			"    <style>\r\n" + 
			"      html, body, #map-canvas {\r\n" + 
			"        height: 100%;\r\n" + 
			"        margin: 0px;\r\n" + 
			"        padding: 0px\r\n" + 
			"      }\r\n" + 
			"      h5 {\r\n" + 
			"        color: white;\r\n" + 
			"        font-family: Arial;\r\n" + 
			"        font-size: 15px\r\n" + 
			"        }" +
			"      #zoomLevel {\r\n" + 
			"            background-color: skyblue; \r\n" + 
			"            color: beige; \r\n" + 
			"            text-align: center; \r\n" + 
			"            font-size: 12px; \r\n" + 
			"            font-family: Verdana;\r\n" + 
			"      }\r\n" + 
			"    </style>"
			;
		
		htmlBlockEnd = "  </head>\r\n" + 
				"  <body>\r\n" + 
				"    <div id=\"zoomLevel\"></div>\r\n" +
				"    <div id=\"map-canvas\"></div>\r\n" + 
				"  </body>\r\n" + 
				"</html>"
				;
		
	}
}	
	













