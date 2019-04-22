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

import com.latidude99.sncxmlreader.model.Panel;
import com.latidude99.sncxmlreader.model.Polygon;
import com.latidude99.sncxmlreader.model.Position;
import com.latidude99.sncxmlreader.model.StandardNavigationChart;
import com.latidude99.sncxmlreader.utils.ConfigPaths;
import com.latidude99.sncxmlreader.utils.FileUtils;

import java.util.*;

public class ScriptBlock {
//	public static String ConfigPaths.CONFIG_PATH.getPath() = "src/main/config/user_data/config.properties";
	private String api_key; // "AIzaSyC_wf8M3SFAJrr6jy_4nK04jUzVgw-TpEY2";

	Map<String, StandardNavigationChart> charts;
	private String script;
	private String scriptStart;
	private String scriptEnd;
	private String scriptMiddle;
	
//	public ScriptBlock() {
//		setScript();
//		}
	
	public ScriptBlock(Map<String, StandardNavigationChart> charts) {
		this.api_key = FileUtils.readApiKey(ConfigPaths.CONFIG.getPath(), "no default key");
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
		Map<String, String> scriptMapAllFoundCharts = new LinkedHashMap<>();
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
		sb.append(addZoomChangeListener());
		sb.append(setBounds());

		return sb.toString();
	}
	
	// does not work in WebView
	private String alertZoomLevel() {
		String level = "window.alert(\"Zoom Level: \" + zoom);";
		return level;
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
		String[] colours = { "#0033cc", "#008080", "#996633", "#990033", "#006666", "#73264d", "#660066", "#000099",
				"#004d00", "#800000", "#006622", "#66194d", "#24478f", "#806000", "#260033", "#992600", "#003300" };
		Random random =new Random();
        int randomNum=random.nextInt(colours.length);
        
        return colours[randomNum];
	}
	
	private Map<String, String> calculateZoomMaxMin(int scale) {
		Map<String, String> maxminLevels = new HashMap<>();
		if(scale < 50_000_000 && scale > 20_000_000) {
			maxminLevels.put("max", "3");
			maxminLevels.put("min", "1");
		}else if(scale < 20_000_001 && scale > 10_000_000) {
			maxminLevels.put("max", "3");
			maxminLevels.put("min", "2");
		}else if(scale < 10_000_001 && scale > 5_000_000) {
			maxminLevels.put("max", "4");
			maxminLevels.put("min", "2");
		}
		else if(scale < 5_000_001 && scale > 2_000_000) {
			maxminLevels.put("max", "5");
			maxminLevels.put("min", "2");
		}
		else if(scale < 2_000_001 && scale > 1_000_000) {
			maxminLevels.put("max", "6");
			maxminLevels.put("min", "3");
		}
		else if(scale < 1_000_001 && scale > 500_000) {
			maxminLevels.put("max", "7");
			maxminLevels.put("min", "4");
		}
		else if(scale < 500_001 && scale > 300_000) {
			maxminLevels.put("max", "8");
			maxminLevels.put("min", "5");
		}
		else if(scale < 300_001 && scale > 100_000) {
			maxminLevels.put("max", "10");
			maxminLevels.put("min", "6");
		}
		else if(scale < 100_001 && scale > 80_000) {
			maxminLevels.put("max", "11");
			maxminLevels.put("min", "7");
		}
		else if(scale < 80_001 && scale > 50_000) {
			maxminLevels.put("max", "12");
			maxminLevels.put("min", "7");
		}
		else if(scale < 50_001 && scale > 30_000) {
			maxminLevels.put("max", "13");
			maxminLevels.put("min", "8");
		}
		else if(scale < 30_001 && scale > 20_000) {
			maxminLevels.put("max", "22");
			maxminLevels.put("min", "8");
		}
		else if(scale < 20_001 && scale > 10_000) {
			maxminLevels.put("max", "22");
			maxminLevels.put("min", "9");
		}
		else if(scale < 10_001 && scale > 8_000) {
			maxminLevels.put("max", "22");
			maxminLevels.put("min", "9");
		}
		else if(scale < 8_001 && scale > 5_000) {
			maxminLevels.put("max", "22");
			maxminLevels.put("min", "9");
		}else if(scale < 5_001 && scale > 1_000) {
			maxminLevels.put("max", "22");
			maxminLevels.put("min", "10");
		}else {
			maxminLevels.put("max", "5");
			maxminLevels.put("min", "1");
		}
		return maxminLevels;
	}
	
	private String setZIndex(String polyName, String zIndex) {
		String index = "\n\n            " + polyName + ".setZIndex(" + zIndex + ");\n\n";
		return index;
	}
	
		
	private Coordinates calculateLabelCoords(Polygon polygon) {
		ChartCentreCalculator calculator = new ChartCentreCalculator();
		Coordinates centreCoords = new Coordinates();
		centreCoords = calculator.calculatePolygonBottomLeftInside(polygon);
		return centreCoords;
	}
	
	
	private Map<String, String> createScriptMapSingleChart(StandardNavigationChart chart){
		Map<String, String> scriptMap = new TreeMap<>();
		String chartColour = getColour();
		List<Polygon> polygons = chart.getMetadata().getGeographicLimit().getPolygons();
		int chartPolygonCount = 0;
		if(chart.getMetadata().getGeographicLimit() != null 
				&& polygons != null) {
			for (Polygon polygon : polygons) {

				if (polygon != null && polygon.getPositions() != null) {
					Map<String, String> params = new TreeMap<>();
					String chartVar = chart.getShortName().trim() + "_" + ++chartPolygonCount + "_polygon";
					int scaleNumber = Integer.parseInt(chart.getMetadata().getScale().trim());
					String chartName = chart.getMetadata().getDatasetTitle().replace("\'", "").replace("\"","");
					String scaleFormatted = String.format("%,d", scaleNumber);
					String latlngArray = createLatLngArray(polygon);
					Coordinates coordsLabel = calculateLabelCoords(polygon);
					String labelPosition = "new google.maps.LatLng(" + coordsLabel.latitude + ", "
							+ coordsLabel.longitude + ")";
					String fontSize = "16px";
					String strokeWeight = "2";
					String fillOpacity = "0.05";
					String zoomMax = calculateZoomMaxMin(scaleNumber).get("max");
					String zoomMin = calculateZoomMaxMin(scaleNumber).get("min");
					
					params.put("type", "chart");
					params.put("chartVar", "chart" + chartVar);
					params.put("chartNumber", chart.getShortName().trim());
					params.put("chartName", chartName);
					params.put("chartScale", "1:" + scaleFormatted);
					params.put("colour", chartColour);
					params.put("latlngArray", latlngArray);
					params.put("labelPosition", labelPosition);
					params.put("fontSize", fontSize);
					params.put("strokeWeight", strokeWeight);
					params.put("fillOpacity", fillOpacity);
					params.put("zoomMax", zoomMax);
					params.put("zoomMin", zoomMin);
					
					String chartNum = createChartNumHTML(params);
					String label = createChartLabel(params);
					String pushLine = addPushMarkerIntoArray(params);
//					String zIndex = setZIndex(params.get("chartVar"), zoomMax);
					String chartNumWithLabel = chartNum + label +  pushLine;
//					System.out.println("chartNum: " + chartNum + "min / max: " + zoomMin + " / " + zoomMax);
					scriptMap.put(params.get("chartVar"), chartNumWithLabel);
				}
			}
		}
		
		if(chart.getMetadata().getPanels() != null) {
//			String panelColour = getColour();
			for(Panel panel : chart.getMetadata().getPanels()) {
				int panelPolygonCount = 0;
				if(panel.getPolygons() != null) {
					for (Polygon polygon : panel.getPolygons()) {
						Map<String, String> params = new TreeMap<>();
						String chartVar = "panel_" + panel.getPanelID() + "_of_" + chart.getShortName()
																+ "_" + ++panelPolygonCount + "_polygon";
						String panelName = panel.getPanelAreaName().replace("\'", "").replace("\"", "");
						int scaleNumber = Integer.parseInt(panel.getPanelScale().trim());
						String scaleFormatted = String.format("%,d", scaleNumber);
						String latlngArray = createLatLngArray(polygon);
						Coordinates coordsLabel = calculateLabelCoords(polygon);
						String labelPosition = "new google.maps.LatLng(" + coordsLabel.latitude + ", "
								+ coordsLabel.longitude + ")";
						String fontSize = "12px";
						String strokeWeight = "1";
						String fillOpacity = "0.1";
						String zoomMax = calculateZoomMaxMin(scaleNumber).get("max");
						String zoomMin = calculateZoomMaxMin(scaleNumber).get("min");
						
						params.put("type", "panel");
						params.put("chartVar", chartVar);
						params.put("chartNumber", chart.getShortName());
						params.put("chartName", panelName);
						params.put("chartScale", "1:" + scaleFormatted);
						params.put("colour", chartColour);
						params.put("latlngArray", latlngArray);
						params.put("labelPosition", labelPosition);
						params.put("fontSize", fontSize);
						params.put("strokeWeight", strokeWeight);
						params.put("fillOpacity", fillOpacity);
						params.put("zoomMax", zoomMax);
						params.put("zoomMin", zoomMin);
						
						String panelNum = createChartNumHTML(params);
						String label = createChartLabel(params);
						String pushLine = addPushMarkerIntoArray(params);
//						String zIndex = setZIndex(params.get("chartVar"), zoomMax);
						String panelNumWithLabel = panelNum + label + pushLine;
//						System.out.println("chartNum: " + panelNumWithLabel);
						scriptMap.put(params.get("chartVar"), panelNumWithLabel);
					}
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
				"                clickable: true,\r\n" +
				"                strokeWeight: '" + params.get("strokeWeight") + "',\r\n" + 
				"                fillColor: '" + params.get("colour") + "',\r\n" + 
				"                fillOpacity: '" + params.get("fillOpacity") + "',\r\n" + 
				"                number: '" + params.get("chartNumber") + "',\r\n" + 
				"                type: '" + params.get("type") + "',\r\n" + 
				"                name: '" + params.get("chartName") + "',\r\n" + 
				"                scale: '" + params.get("chartScale") + "',\r\n" + 
				"                zoomMax: '" + params.get("zoomMax") + "',\r\n" + 
				"                zoomMin: '" + params.get("zoomMin") + "',\r\n" + 
				"                map: map\r\n," + 
				"                zIndex: '" + params.get("zoomMax") + "'\r\n" + 
				"            });\r\n" + 
				"\r\n" + 
				"            google.maps.event.addListener(" + params.get("chartVar") + ", 'click', showInfo);\r\n" + 
				"\r\n" 
				;
		return chartNumHTML;
	}
	
	private String createChartLabel(Map<String, String> params) {
		String label =  "\r\n" +
				"            var marker_" + params.get("chartVar") + " = new google.maps.Marker({\r\n" + 
				"                    map: map,\r\n" + 
				"                    position: " + params.get("labelPosition") + ",\r\n" + 
				"                    icon: \" \",\r\n" + 
				"                    draggable: false,\r\n" +
				"                    clickable: true,\r\n" +
				"                    visible: true,\r\n" +
				"                    zoomMax: '" + params.get("zoomMax") + "',\r\n" + 
				"                    zoomMin: '" + params.get("zoomMin") + "',\r\n" + 
				"                    label: {\r\n" + 
				"                      text: '" + params.get("chartNumber") + "',\r\n" + 
				"                      color: '" + params.get("colour") + "',\r\n" + 
				"                      fontSize: '" + params.get("fontSize") + "',\r\n" + 
				"                      fontShadow: \"6px\",\r\n" + 
				"                      fontWeight: \"regular\"\r\n" + 
				"                    }\r\n" + 
				"              });\r\n" +
				"              \r\n"
				;
		return label;
	}

	private String addPushMarkerIntoArray(Map<String, String> params) {
		String pushLine = "            markers.push(marker_" + params.get("chartVar") + ");\r\n";
		return pushLine;
	}
	
	private String addZoomChangeListener() {
		String listener = "            google.maps.event.addListener(map, 'zoom_changed', function() {\r\n" + 
				"                var zoom = map.getZoom();\r\n" + 
				"        \r\n" + 
				"                for (i = 0; i < polygons.length; i++) {\r\n" + 
				"                       if (zoom < polygons[i].zoomMin) \r\n" + 
				"                           polygons[i].setMap(null);\r\n" + 
				"                       else if (zoom > polygons[i].zoomMax) \r\n" + 
				"                           polygons[i].setMap(null);\r\n" + 
				"                       else \r\n" + 
				"                           polygons[i].setMap(map);\r\n" + 
				"                   }\r\n" + 
				"               for (i = 0; i < markers.length; i++) {\r\n" + 
				"                     \r\n" + 
				"                    if (zoom < markers[i].zoomMin) \r\n" + 
				"                        markers[i].setMap(null);\r\n" + 
				"                    else if (zoom > markers[i].zoomMax) \r\n" + 
				"                       markers[i].setMap(null);\r\n" + 
				"                    else \r\n" + 
				"                       markers[i].setMap(map);\r\n" + 
				"                   }\r\n" + 
				"                   document.getElementById(\"zoomLevel\").innerHTML = 'Zoom Level: ' + zoom;\r\n" + 
				"            });\r\n" 
				;
		return listener;
	}
	
	// used to set zoom levels for markers independently from polygons, not used for now
	private String addZoomChangeMarker() {
		String markerListener = 
				"\r\n" + 
				"            google.maps.event.addListener(map, 'zoom_changed', function() {\r\n" + 
				"                var zoom = map.getZoom();\r\n" + 
				"                   for (i = 0; i < markers.length; i++) {\r\n"
				+ "                      markers[i].setVisible(zoom > 6);\r\n" + 
				"                   }\r\n" + 
				"            });\r\n" + 
				"            \r\n" + 
				"            \r\n" ;
		return markerListener;
	}
	
	private String setBounds() {
		String bounds = 
				"\r\n"+
						"            var bounds= new google.maps.LatLngBounds();\r\n" + 
						"            for (var i=0; i < 1; i++){\r\n" + 
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
						"        }\r\n\r\n"
						;
		return bounds;
	}
	
	private String setScriptStart() {
		scriptStart = "\r\n<script src=\"https://maps.googleapis.com/maps/api/js?key=" + api_key
				+ "&callback=initMap\"></script>\r\n" + 
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
				"            var markers = [];\r\n" + 
				"\r\n" + 
				""
				;
		return scriptStart;
	}
	
	private String setScriptEnd() {
		scriptEnd = "\r\n"+
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

	// only one polygon for, not used for now
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
//		System.out.println("WebPoly: " + sb.toString());
		return sb.toString();
	}

}























