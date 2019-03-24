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
		Map<String, String> scriptMapAllFoundCharts = new TreeMap<>();
		StringBuilder sb = new StringBuilder();
		
		for(StandardNavigationChart chart : charts.values()) {		
			Map<String, String> scriptMapSingleChart = createScriptMapSingleChart(chart);
			scriptMapAllFoundCharts.putAll(scriptMapSingleChart);
		}
		for(String varChartDefinition : scriptMapAllFoundCharts.values()) {
			sb.append(varChartDefinition);
		}
		sb.append("\r\n            var polygons = new Array( ");
		for(String varChartName : scriptMapAllFoundCharts.keySet()) {
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
		String[] colours = {"#0033cc", "#008080", "#996600", "#990033", "#006666",
				"#993366", "#660066", "#000099", "#006600", "#ff0000", "#009933",
				"#cc3399", "#3366cc", "#cc9900", "#cc33ff", "#cc3300", "#003300"};
		Random random =new Random();
        int randomNum=random.nextInt(colours.length);
        
        return colours[randomNum];
	}
	
	
	private Map<String, String> createScriptMapSingleChart(StandardNavigationChart chart){
		Map<String, String> scriptMap = new TreeMap<>();
		
		if(chart.getMetadata().getGeographicLimit() != null 
				&& chart.getMetadata().getGeographicLimit().getPolygons() != null) {
			Map<String, String> params = new TreeMap<>();
			int scaleNumber = Integer.parseInt(chart.getMetadata().getScale().trim());
      		String scaleFormatted = String.format("%,d", scaleNumber);
			params.put("type", "chart");
			params.put("chartVar", "chart" + chart.getShortName());
			params.put("chartNumber", chart.getShortName());
			params.put("chartName", chart.getMetadata().getDatasetTitle());
			params.put("chartScale", "1:" +  scaleFormatted);
			params.put("colour", getColour());
			params.put("latlngArray", createLatLngArray(chart.getMetadata().getGeographicLimit().getPolygons()));
		
			String chartNum = createChartNumHTML(params);
			System.out.println("chartNum: " + chartNum);
			scriptMap.put(params.get("chartVar"), chartNum);
		}
		
		if(chart.getMetadata().getPanels() != null) {
			String panelColour = getColour();
			for(Panel panel : chart.getMetadata().getPanels()) {
				
				if(panel.getPolygon() != null) {
					Map<String, String> params = new TreeMap<>();
					int scaleNumber = Integer.parseInt(panel.getPanelScale().trim());
		      		String scaleFormatted = String.format("%,d", scaleNumber);
					params.put("type", "panel");
					params.put("chartVar", "panel" + panel.getPanelID());
					params.put("chartNumber", chart.getShortName());
					params.put("chartName", panel.getPanelAreaName());
					params.put("chartScale", "1:" +  scaleFormatted);
					params.put("colour", panelColour);
					params.put("latlngArray", createLatLngArray(panel.getPolygon()));
				
					String panelNum = createChartNumHTML(params);
					
					scriptMap.put(params.get("chartVar"), panelNum);
				}
			}
			
		}
		return scriptMap;
	}
	
	
	private String createChartNumHTML(Map<String, String> params) {
		String chartNumHTML = "\r\n" + 
				"            var " + params.get("chartVar") + " = new google.maps.Polygon({\r\n" + 
				"                paths: [\r\n" + 
								 params.get("latlngArray") + "],\r\n" + 
				"                strokeColor: '" + params.get("colour") + "',\r\n" + 
				"                strokeOpacity: 0.8,\r\n" + 
				"                strokeWeight: 1,\r\n" + 
				"                fillColor: '" + params.get("colour") + "',\r\n" + 
				"                fillOpacity: 0.3,\r\n" + 
				"                number: '" + params.get("chartNumber") + "',\r\n" + 
				"                type: '" + params.get("type") + "',\r\n" + 
				"                name: '" + params.get("chartName") + "',\r\n" + 
				"                scale: '" + params.get("chartScale") + "',\r\n" + 
				"                map: map\r\n" + 
				"            });\r\n" + 
				"\r\n" + 
				"            google.maps.event.addListener(" + params.get("chartVar") + ", 'click', showInfo);\r\n" + 
				"\r\n" + 
				""
				;
		return chartNumHTML;
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
				"            var chartInfo = '<b>' + this.number + '</b><br>' +\r\n" + 
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























