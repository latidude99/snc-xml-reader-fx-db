package com.latidude99.sncxmlreader.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.latidude99.sncxmlreader.db.ChartMap;
import com.latidude99.sncxmlreader.model.Panel;
import com.latidude99.sncxmlreader.model.Polygon;
import com.latidude99.sncxmlreader.model.Position;
import com.latidude99.sncxmlreader.model.StandardNavigationChart;

public class ChartCentreCalc {
	public Map<String, Coordinates> chartsCentreCoords;
	
	
	public ChartCentreCalc(Map<String, StandardNavigationChart> charts) {
		chartsCentreCoords = createChartsCentreCoordsMap(charts);
	}
	
	public ChartCentreCalc(List<StandardNavigationChart> charts) {
		chartsCentreCoords = createChartsCentreCoordsMap(charts);
	}
	
	
	private Map<String, Coordinates> createChartsCentreCoordsMap(Map<String, StandardNavigationChart> charts){
		Map<String, Coordinates> chartsCentreCoordsMap = new HashMap<>();
		Coordinates centreCoords = null;
		for(StandardNavigationChart chart : charts.values()) {
			centreCoords = calculateChartCentre(chart);
			chartsCentreCoordsMap.put(chart.getShortName(), centreCoords);
		}
		return chartsCentreCoordsMap;
	}
	
	private Map<String, Coordinates> createChartsCentreCoordsMap(List<StandardNavigationChart> charts){
		Map<String, Coordinates> chartsCentreCoordsMap = new HashMap<>();
		Coordinates centreCoords = null;
		for(StandardNavigationChart chart : charts) {
			centreCoords = calculateChartCentre(chart);
			chartsCentreCoordsMap.put(chart.getShortName(), centreCoords);
		}
		return chartsCentreCoordsMap;
	}
	
	private Coordinates calculateChartCentre(StandardNavigationChart chart) {
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
		
		return new Coordinates(middleLat, middleLng);
	}
	
	private List<Polygon> extractChartPolygons(StandardNavigationChart chart){
		List<Polygon> polygonList = new ArrayList<>();
		if(chart.getMetadata().getGeographicLimit() != null 
				&& chart.getMetadata().getGeographicLimit().getPolygons() != null) {
			for(Polygon polygon : chart.getMetadata().getGeographicLimit().getPolygons())
				polygonList.add(polygon);
		}
		if(chart.getMetadata().getPanels() != null) {
			for(Panel panel : chart.getMetadata().getPanels())
				if(panel.getPolygon() != null) {
					polygonList.add(panel.getPolygon());
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
		double max = 0;
		for(double d : list) {
			if(max < d)
				max = d;
		}
		return max;
	}
	
	private double findMin(List<Double> list) {
		double min = 0;
		for(double d : list) {
			if(min > d)
				min = d;
		}
		return min;
	}
	

}

























