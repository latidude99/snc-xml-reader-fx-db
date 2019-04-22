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

//import uk.me.jstott.jcoord.LatLng;

public class ChartCentreCalculator {
	public Map<String, Coordinates> chartsCentreCoords;
	
	public ChartCentreCalculator() {}
	
	public ChartCentreCalculator(Map<String, StandardNavigationChart> charts) {
		chartsCentreCoords = createChartsCentreCoordsMap(charts);
//		calcDistance(charts);
	}
	public ChartCentreCalculator(List<StandardNavigationChart> charts) {
		chartsCentreCoords = createChartsCentreCoordsMap(charts);
	}
/*	
	private void calcDistance(Map<String, StandardNavigationChart> charts) {
		LatLng latlng1 = new LatLng(createChartsCentreCoordsMap(charts).get("5").latitude,
												createChartsCentreCoordsMap(charts).get("5").longitude);
		LatLng latlng2 = new LatLng(createChartsCentreCoordsMap(charts).get("6").latitude,
				createChartsCentreCoordsMap(charts).get("6").longitude);
		double distance = latlng1.distance(latlng2);
		System.out.println("Distance 5 - 6: " + distance);
	}
*/	
	public Coordinates calculatePolygonCentre(Polygon polygon) {
		List<Coordinates> coords = new ArrayList<>();
		Coordinates polyCentre = null;
		coords = polygon2CoordsList(polygon);
		polyCentre = calculateCentreFromCoords(coords);
		
		return polyCentre;
	}
	
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
		
		double bottomLeftLat = latMin;// + (latMax - latMin) * 0.02;
		double bottomLeftLng = lngMin + (lngMax - lngMin) * 0.05;
		
//		System.out.println("latMin: " +latMin + ", " +"latMax: " + latMax);
			
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
	
	public Coordinates calculateChartCentre(StandardNavigationChart chart) {
		List<Coordinates> polyCentreList = new ArrayList<>();
		List<Coordinates> coords = new ArrayList<>();
		Coordinates polyCentre = null;
		Coordinates chartCentre = null;
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
		
//		System.out.println("latMin: " +latMin + ", " +"latMax: " + latMax);
//		System.out.println("lngMin: " +lngMin + ", " +"lngMax: " + lngMax);
		
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

























