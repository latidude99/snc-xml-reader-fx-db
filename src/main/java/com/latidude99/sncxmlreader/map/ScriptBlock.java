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

/*
 * Generates script for displaying charts as polygons using Google Maps Javascript API
 */
public class ScriptBlock {
	private String api_key;

	Map<String, StandardNavigationChart> charts;
	private String scriptStart;
	private String scriptEnd;
	private String scriptMiddle;

	public ScriptBlock(Map<String, StandardNavigationChart> charts) {
		this.api_key = FileUtils.readApiKey(ConfigPaths.CONFIG.getPath(), "no key");
		this.charts = charts;
	}
	
	public String getScript() {
		scriptStart = setScriptStart();
		scriptMiddle = createScriptMiddle(charts);
		scriptEnd = setScriptEnd();
		String script = scriptStart + scriptMiddle + scriptEnd;
		return script;
	}

	/*
	 * Script middle part composition.
	 */
	private String createScriptMiddle(Map<String, StandardNavigationChart> charts) {
		Map<String, String> scriptMapAllFoundCharts = new LinkedHashMap<>();
		StringBuilder sb = new StringBuilder();

		// creates a Map with a script for each chart
		for(StandardNavigationChart chart : charts.values()) {		
			Map<String, String> scriptMapSingleChart = createScriptMapSingleChart(chart);
			scriptMapAllFoundCharts.putAll(scriptMapSingleChart);
		}

		// creates a String for all charts
		for(String varChartDefinition : scriptMapAllFoundCharts.values()) {
			sb.append(varChartDefinition);
		}

		// adds charts to an Array
		sb.append("\r\n            var polygons = new Array( ");
		for(String varChartName : scriptMapAllFoundCharts.keySet()) {
			sb.append(varChartName +", ");
		}
		sb.append(" );\r\n\r\n");

		sb.append(addZoomChangeListener());
		sb.append(setBounds());

		return sb.toString();
	}

	/*
	 * Prepares parameters map for each chart to inject it in the script making method.
	 */
	private Map<String, String> createScriptMapSingleChart(StandardNavigationChart chart){
		Map<String, String> scriptMap = new TreeMap<>();
		String chartColour = getColour();
		List<Polygon> polygons = chart.getMetadata().getGeographicLimit().getPolygons();
		int chartPolygonCount = 0;

		// for main chart's panel
		if(chart.getMetadata().getGeographicLimit() != null
				&& polygons != null) {
			for (Polygon polygon : polygons) {

				if (polygon != null && polygon.getPositions() != null) {
					Map<String, String> params = new TreeMap<>();
					String chartVar = chart.getShortName().trim() + "_" + ++chartPolygonCount + "_polygon";
					int scaleNumber = Integer.parseInt(chart.getMetadata().getScale().trim());
					String chartName = chart.getMetadata()
							.getDatasetTitle()
							.replace("\'", "")
							.replace("\"","");
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
					String chartNumWithLabel = chartNum + label +  pushLine;
					scriptMap.put(params.get("chartVar"), chartNumWithLabel);
				}
			}
		}

		// for additional panels
		if(chart.getMetadata().getPanels() != null) {
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
						String panelNumWithLabel = panelNum + label + pushLine;
						scriptMap.put(params.get("chartVar"), panelNumWithLabel);
					}
				}
			}
		}
		return scriptMap;
	}


	/*
	 * Injects individual charts data to create script entry for each chart
	 */
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

	/*
	 * Creates part of the script responsible for levels in Google Maps that charts
	 * will be drawn on according to their scale. The goal is to not to try and draw
	 * all the charts that are supposed to be displayed but only those relevant to
	 * the zoom level and chart coverage.
	 */
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

	/*
	 * Sets zoom levels for markers independently from polygons, not used for now
	 * but this idea may be revisited in the future. Possible reason for this would
	 * be showing labels with chart numbers on much lower level (smaller map scale)
	 * than the charts polygons. This way one can easily spot where a large scale
	 * (small coverage) chart is and what number when zoomed far out. Turns out
	 * this will clutter the view a bit too much for my liking so the solution
	 * is shelved until better idea comes to mind.
	 */
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

	/**
	 * Sets the bounds for initial map view. Takes only first polygon into
	 * account to focus the view on the chart that was first on the list
	 * charts searched by the user. This has proved being the most useful
	 * and desired behaviour for a small but competent test group.
	 * Alternatively, when searching for a range of charts the initial
	 * map view may be set to encompass all the whole searched range
	 * (this may be implemented when required).
	 */
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

	/*
	 * Creates the starting part of the script, same for all cases.
	 */
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

	/*
	 * Creates the ending part of the script, same for all cases.
	 */
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

	/*
	 * Creates a String with Javascript array of all polygon's vertexes.
	 * Needed for creating polygons by Google Maps Javascript API.
	 */
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

	/*
	 * Positions label in the bottom-tight corner of a chart.
	 */
	private Coordinates calculateLabelCoords(Polygon polygon) {
		ChartCentreCalculator calculator = new ChartCentreCalculator();
		Coordinates centreCoords;
		centreCoords = calculator.calculatePolygonBottomLeftInside(polygon);
		return centreCoords;
	}

	/*
	 * Colour pool for polygons to draw from.
	 */
	private String getColour() {
		String[] colours = { "#0033cc", "#008080", "#996633", "#990033", "#006666", "#73264d", "#660066", "#000099",
				"#004d00", "#800000", "#006622", "#66194d", "#24478f", "#806000", "#260033", "#992600", "#003300" };
		Random random =new Random();
		int randomNum=random.nextInt(colours.length);

		return colours[randomNum];
	}

	/*
	 * Scales thresholds for displaying chart polygons. Sets visibility
	 * levels according to chart's scale. Uses Google Maps Levels, where
	 * 1 is the smallest and 22 the largest zoom level.
	 */
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


}























