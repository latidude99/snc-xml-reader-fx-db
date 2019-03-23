package com.latidude99.sncxmlreader.web;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.TreeMap;

import com.latidude99.sncxmlreader.model.Panel;
import com.latidude99.sncxmlreader.model.Polygon;
import com.latidude99.sncxmlreader.model.Position;
import com.latidude99.sncxmlreader.model.StandardNavigationChart;

public class ScriptBlock {
	Map<String, StandardNavigationChart> charts;
	private String script;
	private String scriptStart;
	private String scriptEnd;
	private String scriptMiddle;
	
//	public ScriptBlock() {
//		setScript();
//		}
	
	public ScriptBlock(Map<String, StandardNavigationChart> charts) {
		this.charts = charts;
	}
	
	public String getScript() {
//		setScript();
		scriptStart = setScriptStart();
		scriptMiddle = createScriptMiddle(charts);
		scriptEnd = setScriptEnd();
		script = scriptStart + scriptMiddle + scriptEnd;
		return script;
	}
	
	private String createScriptMiddle(Map<String, StandardNavigationChart> charts) {
		Map<String, String> scriptMap = new TreeMap<>();
		StringBuilder sb = new StringBuilder();
		
		for(StandardNavigationChart chart : charts.values()) {		
			
			
			if(chart.getMetadata().getGeographicLimit() != null 
					&& chart.getMetadata().getGeographicLimit().getPolygons() != null) {
				String type = "Chart";
				String chartVar = "chart" + chart.getShortName();
				String chartNumber = chart.getShortName();
				String chartName = chart.getMetadata().getDatasetTitle();
				int scaleNumber = Integer.parseInt(chart.getMetadata().getScale().trim());
          		String scaleFormatted = String.format("%,d", scaleNumber);
				String chartScale = "1:" +  scaleFormatted;
				String colour = getColour();
				String latlngArray = createLatLngArray(chart.getMetadata().getGeographicLimit().getPolygons());
				String chartNum = "\r\n" + 
						"            var " + chartVar + " = new google.maps.Polygon({\r\n" + 
						"                paths: [\r\n" + 
										 latlngArray + "],\r\n" + 
						"                strokeColor: '" + colour + "',\r\n" + 
						"                strokeOpacity: 0.8,\r\n" + 
						"                strokeWeight: 1,\r\n" + 
						"                fillColor: '" + colour + "',\r\n" + 
						"                fillOpacity: 0.3,\r\n" + 
						"                number: '" + chartNumber + "',\r\n" + 
						"                type: '" + type + "',\r\n" + 
						"                name: '" + chartName + "',\r\n" + 
						"                scale: '" + chartScale + "',\r\n" + 
						"                map: map\r\n" + 
						"            });\r\n" + 
						"\r\n" + 
						"            google.maps.event.addListener(" + chartVar + ", 'click', showInfo);\r\n" + 
						"\r\n" + 
						""
						;
		
				System.out.println("chartNum: " + chartNum);
				scriptMap.put(chartVar, chartNum);
			}
			
			if(chart.getMetadata().getPanels() != null) {
				String panelColour = getColour();
				for(Panel panel : chart.getMetadata().getPanels()) {
					
					if(panel.getPolygon() != null) {
						String type = "Panel";
						String panelVar = "panel" + panel.getPanelID();
						String chartNumber = chart.getShortName();
						String chartName = chart.getMetadata().getDatasetTitle();
						String panelName = panel.getPanelAreaName();
						int scaleNumber = Integer.parseInt(panel.getPanelScale().trim());
		          		String scaleFormatted = String.format("%,d", scaleNumber);
						String panelScale = "1:" + scaleFormatted;
						String latlngArray = createLatLngArray(panel.getPolygon());
						String panelNum = "\r\n" + 
								"            var " + panelVar + " = new google.maps.Polygon({\r\n" + 
								"                paths: [\r\n" + 
												 latlngArray + "],\r\n" + 
								"                strokeColor: '" + panelColour + "',\r\n" + 
								"                strokeOpacity: 0.8,\r\n" + 
								"                strokeWeight: 1,\r\n" + 
								"                fillColor: '" + panelColour + "',\r\n" + 
								"                fillOpacity: 0.3,\r\n" + 
								"                number: '" + chartNumber + "',\r\n" + 
								"                type: '" + type + "',\r\n" + 
								"                name: '" + panelName + "',\r\n" + 
								"                scale: '" + panelScale + "',\r\n" + 
								"                map: map\r\n" + 
								"            });\r\n" + 
								"\r\n" + 
								"            google.maps.event.addListener(" + panelVar + ", 'click', showInfo);\r\n" + 
								"\r\n" + 
								""
								;
				
//						System.out.println("chartNum: " + panelNum);
						scriptMap.put(panelVar, panelNum);
					}
				}
				
			}
			
			
		}
		for(String varChartDefinition : scriptMap.values()) {
			sb.append(varChartDefinition);
		}
		sb.append("\r\n            var polygons = new Array( ");
		for(String varChartName : scriptMap.keySet()) {
			sb.append(varChartName +", ");
		}
		sb.append(" );\r\n\r\n");
		return sb.toString();
	}
	
	private String createLatLngArray(Polygon polygon) {
		StringBuilder sb = new StringBuilder();
		List<Position> positions = polygon.getPositions();
		int positionsNum = polygon.getPositions().size();
        for(int i = 0; i < positionsNum -1 ; i++) {
        	sb.append("                new google.maps.LatLng(" + 
        				positions.get(i).getLatitude() + "," + positions.get(i).getLongitude() + ")");
        	if(i !=  positionsNum - 2)
        		sb.append(",\r\n");
        }
        return sb.toString();
	}
		
	private String createLatLngArray(List<Polygon> polygons) {
		StringBuilder sb = new StringBuilder();
		int count = 0;
		
			for(Polygon poly :polygons) {
				count++;
				if(count > 1)
					break;
				List<Position> positions = poly.getPositions();
				int positionsNum = poly.getPositions().size();
          		for(int i = 0; i < positionsNum -1 ; i++) {
          			sb.append("                new google.maps.LatLng(" + 
          						positions.get(i).getLatitude() + "," + positions.get(i).getLongitude() + ")");
          			if(i !=  positionsNum - 2)
          				sb.append(",\r\n");
          		}
          	}
		return sb.toString();
	}
	
	private String getColour() {
		String[] colours = {"#0099ff", "##33cccc", "##cc6600", "#990033", "#669999",
				"#ff3399", "#660066", "#000099", "#00cc00", "#ff6600", "#009933",
				"#cc3399", "#3366cc", "#ffcccc", "#cc66ff", "#cc3300", "#003300"};
		Random random =new Random();
        int randomNum=random.nextInt(colours.length);
        
        return colours[randomNum];
	}
	
	
	private String setScriptStart() {
		scriptStart = "\r\n<script src=\"https://maps.googleapis.com/maps/api/js?key=AIzaSyCbeTjn_d4C-OFFJoRbmkfNfvyc7QjkAoM&callback=initMap\"></script>\r\n" + 
				"    <script>\r\n" + 
				"\r\n" + 
				"        var map;\r\n" + 
				"        var infoWindow;\r\n" + 
				"\r\n" + 
				"        function initialize() {\r\n" + 
				"            var mapOptions = {\r\n" + 
				"                zoom: 5,\r\n" + 
				"                center: new google.maps.LatLng(0.00, 0.00),\r\n" + 
				"                mapTypeId: google.maps.MapTypeId.ROAD\r\n" + 
				"            };\r\n" + 
				"\r\n" + 
				"            map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);\r\n" + 
				"            infoWindow = new google.maps.InfoWindow();\r\n" + 
				"\r\n" + 
				""
				;
		return scriptStart;
	}
	
	private String setScriptEnd() {
		scriptEnd = "\r\n" + 
				"            var bounds= new google.maps.LatLngBounds();\r\n" + 
				"            for (var i=0; i < polygons.length; i++){\r\n" + 
				"                var paths = polygons[i].getPaths();\r\n" + 
				"                paths.forEach(function(path){\r\n" + 
				"                   var ar = path.getArray();\r\n" + 
				"                   for(var i=0, l = ar.length; i <l; i++){\r\n" + 
				"                      bounds.extend(ar[i]);\r\n" + 
				"                   }\r\n" + 
				"                })\r\n" + 
				"            }\r\n" + 
				"            map.fitBounds(bounds)\r\n" + 
				"            map.setCenter(bounds.getCenter());\r\n" + 
				"        }\r\n" + 
				"\r\n" + 
				"        function showInfo(event) {\r\n" + 
				"            var vertices = this.getPath();\r\n" + 
				"\r\n" + 
				"            var chartInfo = '<b>' + this.number + '</b><br><br>' +\r\n" + 
				"                            '<sup>' +    this.type + '</sup><br>' +\r\n" + 
				"                                this.name + '</b><br>' +\r\n" + 
				"                                this.scale + '</b><br>';\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"            infoWindow.setContent(chartInfo);\r\n" + 
				"            infoWindow.setPosition(event.latLng);\r\n" + 
				"\r\n" + 
				"            infoWindow.open(map);\r\n" + 
				"        }\r\n" + 
				"\r\n" + 
				"        google.maps.event.addDomListener(window, 'load', initialize);\r\n" + 
				"\r\n" + 
				"    </script>\r\n"
				;
		return scriptEnd;
	}
	
	
	
	
	
	private void setScript() {	
		script = "    <script src=\"https://maps.googleapis.com/maps/api/js?key=AIzaSyCbeTjn_d4C-OFFJoRbmkfNfvyc7QjkAoM&callback=initMap\"></script>\r\n" + 
				"    <script>\r\n" + 
				"\r\n" + 
				"        var map;\r\n" + 
				"        var infoWindow;\r\n" + 
				"\r\n" + 
				"        function initialize() {\r\n" + 
				"            var mapOptions = {\r\n" + 
				"                zoom: 5,\r\n" + 
				"                center: new google.maps.LatLng(0.00, 0.00),\r\n" + 
				"                mapTypeId: google.maps.MapTypeId.ROAD\r\n" + 
				"            };\r\n" + 
				"\r\n" + 
				"            map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);\r\n" + 
				"            infoWindow = new google.maps.InfoWindow();\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"            var chartNumber1 = new google.maps.Polygon({\r\n" + 
				"                paths: [\r\n" + 
				"                new google.maps.LatLng(25.774252, -80.190262),\r\n" + 
				"                new google.maps.LatLng(18.466465, -66.118292),\r\n" + 
				"                new google.maps.LatLng(32.321384, -64.75737)],\r\n" + 
				"                strokeColor: '#FF0000',\r\n" + 
				"                strokeOpacity: 0.8,\r\n" + 
				"                strokeWeight: 1,\r\n" + 
				"                fillColor: '#FF0000',\r\n" + 
				"                fillOpacity: 0.35,\r\n" + 
				"                number: 'number 1',\r\n" + 
				"                name: 'name 1',\r\n" + 
				"                map: map\r\n" + 
				"            });\r\n" + 
				"\r\n" + 
				"            google.maps.event.addListener(chartNumber1, 'click', showInfo);\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"            var chartNumber2 = new google.maps.Polygon({\r\n" + 
				"                paths: [\r\n" + 
				"                new google.maps.LatLng(40.774252, -95.190262),\r\n" + 
				"                new google.maps.LatLng(33.466465, -81.118292),\r\n" + 
				"                new google.maps.LatLng(47.321384, -79.75737)],\r\n" + 
				"                strokeColor: '#008700',\r\n" + 
				"                strokeOpacity: 0.8,\r\n" + 
				"                strokeWeight: 1,\r\n" + 
				"                fillColor: '#008700',\r\n" + 
				"                fillOpacity: 0.35,\r\n" + 
				"                number: 'number 2',\r\n" + 
				"                name: 'name 2', // dynamic, not an official API property..\r\n" + 
				"                map: map\r\n" + 
				"            });\r\n" + 
				"\r\n" + 
				"            google.maps.event.addListener(chartNumber2, 'click', showInfo);\r\n" + 
				"\r\n" + 
				"            var polygons = new Array( chartNumber1, chartNumber2 );\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"            var bounds= new google.maps.LatLngBounds();\r\n" + 
				"            for (var i=0; i < polygons.length; i++){\r\n" + 
				"                var paths = polygons[i].getPaths();\r\n" + 
				"                paths.forEach(function(path){\r\n" + 
				"                   var ar = path.getArray();\r\n" + 
				"                   for(var i=0, l = ar.length; i <l; i++){\r\n" + 
				"                      bounds.extend(ar[i]);\r\n" + 
				"                   }\r\n" + 
				"                })\r\n" + 
				"            }\r\n" + 
				"            map.fitBounds(bounds)\r\n" + 
				"        //   map.setCenter(bounds.getCenter());\r\n" + 
				"        }\r\n" + 
				"\r\n" + 
				"        function showInfo(event) {\r\n" + 
				"            var vertices = this.getPath();\r\n" + 
				"\r\n" + 
				"            var contentString = '<b>' + this.number + '</b><br><br>' +\r\n" + 
				"                                        this.name + '<br>';\r\n" + 
				"\r\n" + 
				"            for (var i = 0; i < vertices.getLength(); i++) {\r\n" + 
				"                var xy = vertices.getAt(i);\r\n" + 
				"                contentString += '<br>' + 'Coordinate ' + i + ':<br>' + xy.lat() + ',' + xy.lng();\r\n" + 
				"            }\r\n" + 
				"\r\n" + 
				"            infoWindow.setContent(contentString);\r\n" + 
				"            infoWindow.setPosition(event.latLng);\r\n" + 
				"\r\n" + 
				"            infoWindow.open(map);\r\n" + 
				"        }\r\n" + 
				"\r\n" + 
				"        google.maps.event.addDomListener(window, 'load', initialize);\r\n" + 
				"\r\n" + 
				"    </script>"
				;
	}
	
	// only one polygon for now
	private String createScriptForOne(StandardNavigationChart chart) {
		int count = 0;
		StringBuilder sb = new StringBuilder();
		sb.append("");
		if(chart.getMetadata().getGeographicLimit() != null 
				&& chart.getMetadata().getGeographicLimit().getPolygons() != null)
			for(Polygon polygon : chart.getMetadata().getGeographicLimit().getPolygons()) {
				count++;
				if(count > 1)
					break;
				List<Position> positions = polygon.getPositions();
				int positionsNum = polygon.getPositions().size();
          		for(int i = 0; i < positionsNum; i++) {
          			sb.append("{lat: " + positions.get(i).getLatitude() + ", lng: " + positions.get(i).getLongitude() + "}");
          			if(i !=  positionsNum - 1) {
          				sb.append(",\r\n");
          			}else {
          				sb.append("\r\n");
          			}
          		}
          	}
		System.out.println("WebPoly: " + sb.toString());
		return sb.toString();
	}

}























