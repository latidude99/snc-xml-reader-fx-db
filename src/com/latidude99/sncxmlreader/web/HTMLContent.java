package com.latidude99.sncxmlreader.web;

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
				"    <div id=\"zoomLevel\"></div>" +
				"    <div id=\"map-canvas\"></div>\r\n" + 
				"  </body>\r\n" + 
				"</html>"
				;
		
	}
}	
	













