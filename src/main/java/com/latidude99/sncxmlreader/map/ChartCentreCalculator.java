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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Calculates centre coordinates (latitude, longitude) of a chart including
 * all its polygons (panels). Also coordinates of the bottom-left corner
 * (used for placing a label with the chart's number)
 */
public class ChartCentreCalculator {
	public Map<String, Coordinates> chartsCentreCoords;
	
	public ChartCentreCalculator() {}

	public ChartCentreCalculator(Map<String, StandardNavigationChart> charts) {
		chartsCentreCoords = createChartsCentreCoordsMap(charts);
	}

	public ChartCentreCalculator(List<StandardNavigationChart> charts) {
		chartsCentreCoords = createChartsCentreCoordsMap(charts);
	}

	public Coordinates calculatePolygonCentre(Polygon polygon) {
		List<Coordinates> coords;
		Coordinates polyCentre = null;
		coords = polygon2CoordsList(polygon);
		polyCentre = calculateCentreFromCoords(coords);
		return polyCentre;
	}

	/*
	 * Used for placing a label (chart's number)
	 */
	public Coordinates calculatePolygonBottomLeftInside(Polygon polygon) {
		List<Coordinates> coords = new ArrayList<>();
		coords = polygon2CoordsList(polygon);
		List<Double> latitudes = new ArrayList<>();
		List<Double> longitudes = new ArrayList<>();
		for(Coordinates coord : coords) {
			latitudes.add(coord.latitude);
			longitudes.add(coord.longitude);
		}
		double latMax = findMax(latitudes);
		double latMin = findMin(latitudes);
		double lngMax = findMax(longitudes);
		double lngMin = findMin(longitudes);

		// Google Maps Label Markers have the insetion point below the text
		// so no vertical (latitude) adjustment is needed
		double bottomLeftLat = latMin;

		//This formula is a compromise: it will still overlap the chart's
		// boundry when zoomed out more than a few levels from the initial zoom level
		double bottomLeftLng = lngMin + (lngMax - lngMin) * 0.05;

		return new Coordinates(bottomLeftLat, bottomLeftLng);
	}
	
	public Map<String, Coordinates> createChartsCentreCoordsMap(Map<String, StandardNavigationChart> charts){
		Map<String, Coordinates> chartsCentreCoordsMap = new HashMap<>();
		Coordinates centreCoords = null;
		for(StandardNavigationChart chart : charts.values()) {
			centreCoords = calculateChartCentre(chart);
			chartsCentreCoordsMap.put(chart.getShortName(), centreCoords);
		}
		return chartsCentreCoordsMap;
	}
	
	public Map<String, Coordinates> createChartsCentreCoordsMap(List<StandardNavigationChart> charts){
		Map<String, Coordinates> chartsCentreCoordsMap = new HashMap<>();
		Coordinates centreCoords = null;
		for(StandardNavigationChart chart : charts) {
			centreCoords = calculateChartCentre(chart);
			chartsCentreCoordsMap.put(chart.getShortName(), centreCoords);
		}
		return chartsCentreCoordsMap;
	}

	/*
	 * Charts may have many polygons (panels). This method calculates
	 * the centre of all of them (which may not be inside any of the polygons).
	 */
	public Coordinates calculateChartCentre(StandardNavigationChart chart) {
		List<Coordinates> polyCentreList = new ArrayList<>();
		List<Coordinates> coords;
		Coordinates polyCentre;
		Coordinates chartCentre;
		List<Polygon> polygonList = extractChartPolygons(chart);
		for(Polygon poly : polygonList) {
			coords = polygon2CoordsList(poly);
			polyCentre = calculateCentreFromCoords(coords);
			polyCentreList.add(polyCentre);
		}
		chartCentre = calculateCentreFromCoords(polyCentreList);
		return chartCentre;
	}
	
	private Coordinates calculateCentreFromCoords(List<Coordinates> coords) {
		List<Double> latitudes = new ArrayList<>();
		List<Double> longitudes = new ArrayList<>();
		for(Coordinates coord : coords) {
			latitudes.add(coord.latitude);
			longitudes.add(coord.longitude);
		}
		double latMax = findMax(latitudes);
		double latMin = findMin(latitudes);
		double lngMax = findMax(longitudes);
		double lngMin = findMin(longitudes);
		
		double middleLat = (latMax +latMin) / 2;
		double middleLng = (lngMax + lngMin) /2;

		return new Coordinates(middleLat, middleLng);
	}	

	
	public List<Polygon> extractChartPolygons(StandardNavigationChart chart){
		List<Polygon> polygonList = new ArrayList<>();
		if(chart.getMetadata().getGeographicLimit() != null 
				&& chart.getMetadata().getGeographicLimit().getPolygons() != null) {
			for(Polygon polygon : chart.getMetadata().getGeographicLimit().getPolygons())
				polygonList.add(polygon);
		}
		if(chart.getMetadata().getPanels() != null) {
			for(Panel panel : chart.getMetadata().getPanels())
				if (panel.getPolygons() != null) {
					polygonList.add(panel.getPolygons().get(0));
				}
		}
		return polygonList;
	}
	
	private List<Coordinates> polygon2CoordsList(Polygon poly){
		List<Coordinates> coords = new ArrayList<>();
		for(Position position : poly.getPositions()) {
			Coordinates  coordinates = new Coordinates(position.getLatitude(), position.getLongitude());
			coords.add(coordinates);
		}
		return coords;
	}


	/*
	 * (-)180 degrees is the maximum (minimum) value for Longitude.
	 * It is (-)90 for Latitude but used the same for both for clarity
	 * (does not make any difference in this case).
	 */
	private double findMax(List<Double> list) {
		double max = -180;
		for(double d : list) {
			if(max < d)
				max = d;
		}
		return max;
	}
	
	private double findMin(List<Double> list) {
		double min = 180;
		for(double d : list) {
			if(min > d)
				min = d;
		}
		return min;
	}
	

}

























