package com.latidude99.sncxmlreader.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.latidude99.sncxmlreader.model.Panel;
import com.latidude99.sncxmlreader.model.Polygon;
import com.latidude99.sncxmlreader.model.Position;
import com.latidude99.sncxmlreader.model.StandardNavigationChart;

import uk.me.jstott.jcoord.LatLng;

//import uk.me.jstott.jcoord.LatLng;

public class ChartProximityCalculator {
	ChartCentreCalculator chartCentreCalculator = new ChartCentreCalculator();
	public Map<String, StandardNavigationChart> chartsOverlaping;
	
	public ChartProximityCalculator() {}
	
	public Map<String, StandardNavigationChart> totalChartsOverlapMap(Map<String, StandardNavigationChart> chartsInputFound,
																			Map<String, StandardNavigationChart> chartsToCheck){
		Map<String, StandardNavigationChart> chartsProximalTotal = new LinkedHashMap<>();
		Map<String, StandardNavigationChart> chartsProximalSingle = new LinkedHashMap<>();
		chartsProximalTotal.putAll(chartsInputFound);
		for(StandardNavigationChart chartInputFound : chartsInputFound.values()) {
			chartsProximalSingle = singleChartOverlapMap(chartInputFound, chartsToCheck);
			chartsProximalTotal.putAll(chartsProximalSingle);
		}
		return chartsProximalTotal;
	}
	
	/*******************************************************************************************************/
	
	public Map<String, StandardNavigationChart> singleChartOverlapMap(StandardNavigationChart chartInputFound, 
														Map<String, StandardNavigationChart> chartsToCheck){
		Map<String, StandardNavigationChart> chartsProximal = new LinkedHashMap<>();
//		chartsProximal.put(chartSearched.getShortName(), chartInputFound);
		for(StandardNavigationChart chartToCheck : chartsToCheck.values()) {
			
			if(isOverapping(chartInputFound, chartToCheck) 
					|| isWithinDistance(1500, chartInputFound, chartToCheck)) {
				
//				System.out.println("isOverapping(chartInputFound, chartToCheck) " + isOverapping(chartInputFound, chartToCheck));
				chartsProximal.put(chartToCheck.getShortName(), chartToCheck);
			}
//			System.out.println("chartsProximal " + chartsProximal.size());
		}
		return chartsProximal;
	}
	

	/*******************************************************************************************************/

	private boolean isWithinScale(int factor, StandardNavigationChart chart1, StandardNavigationChart chart2) {
		int scaleChart1 = getMinScale(chart1);
		int scaleChart2 = getMinScale(chart2);
		System.out.println(scaleChart1 + ", " + scaleChart2);
		if((scaleChart1 * factor > scaleChart2) || (scaleChart1 / factor < scaleChart2))
			return true;
		else	
			return false;
	}
	
	
	private boolean isOverapping(StandardNavigationChart chart1, StandardNavigationChart chart2) {
		Map<String, Double> chart1MinMax = calculateChartMinMax(chart1);
		Map<String, Double> chart2MinMax = calculateChartMinMax(chart2);
		
		double chart1LatMin = chart1MinMax.get("latMin");
		double chart1LatMax = chart1MinMax.get("latMax");
		double chart1LngMin = chart1MinMax.get("lngMin");
		double chart1LngMax = chart1MinMax.get("lngMax");
		
		double chart2LatMin = chart2MinMax.get("latMin");
		double chart2LatMax = chart2MinMax.get("latMax");
		double chart2LngMin = chart2MinMax.get("lngMin");
		double chart2LngMax = chart2MinMax.get("lngMax");
		
		if((chart1LatMin > chart2LatMax)
				|| (chart1LatMax < chart2LatMin) 
				|| (chart1LngMin > chart2LngMax) 
				|| (chart1LngMax < chart2LngMin)) {
/*			System.out.print("\n\nchart1LatMin < chart2LatMax: " + chart1LatMin + " < " + chart2LatMax + " || ");
			System.out.println("chart1LatMax > chart2LatMin: " + chart1LatMax + " > " + chart2LatMin);
			System.out.println("&&");
			System.out.print("chart1LngMin < chart2LngMax: " + chart1LngMin + " < " + chart2LngMax + " || ");
			System.out.println("chart1LngMax > chart2LngMin: " + chart1LngMax + " > " + chart2LngMin);
*/			return false;
		}
		else
			return true;
	}
	

	private Map<String, Double> calculateChartMinMax(StandardNavigationChart chart) {
		List<Double> latitudes = new ArrayList<>();
		List<Double> longitudes = new ArrayList<>();
		List<Polygon> polygons = extractChartPolygons(chart);
		for(Polygon polygon : polygons) {
			for(Position position : polygon.getPositions()) {
				latitudes.add(Double.parseDouble(position.getLatitude().trim()));
				longitudes.add(Double.parseDouble(position.getLongitude().trim()));
			}
		}
		Map<String, Double> minmaxLatLng = new HashMap<>();
		minmaxLatLng.put("latMin", findMin(latitudes));
		minmaxLatLng.put("latMax", findMax(latitudes));
		minmaxLatLng.put("lngMin", findMin(longitudes));
		minmaxLatLng.put("lngMax", findMax(longitudes));
		
		return minmaxLatLng;
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
				if (panel.getPolygons() != null) {
					for(Polygon poly : panel.getPolygons())
					polygonList.add(poly);
				}
		}
		return polygonList;
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
		double min = 90;
		for(double d : list) {
			if(min > d)
				min = d;
		}
		return min;
	}
	
	private boolean isWithinDistance(int distance, StandardNavigationChart chart1, StandardNavigationChart chart2) {
		if(calcDistance(chart1, chart2) < distance)
			return true;
		else
			return false;
	}
	
	// calculates disitance between centres of 2 charts (including all panels), in km	
	private double calcDistance(StandardNavigationChart chart1, StandardNavigationChart chart2) {
		LatLng latlng1 = new LatLng(chartCentreCalculator.calculateChartCentre(chart1).latitude,
									chartCentreCalculator.calculateChartCentre(chart1).longitude);
		LatLng latlng2 = new LatLng(chartCentreCalculator.calculateChartCentre(chart2).latitude,
									chartCentreCalculator.calculateChartCentre(chart2).longitude);
		double distance = latlng1.distance(latlng2);
//		System.out.println("Distance" + distance);
		return distance;
	}
	
	public int getMinScale(StandardNavigationChart chart) {
		int scale = 0;
		int panelScale = 0;
		
		if(chart.getMetadata().getScale() != null && !"".equals(chart.getMetadata().getScale().trim())) { 
			scale = Integer.parseInt(chart.getMetadata().getScale().trim());
			System.out.println("scaleMain: " + scale);
		}
		if(chart.getMetadata().getPanels() != null) {
			for(Panel panel : chart.getMetadata().getPanels()) {
				panelScale = Integer.parseInt(panel.getPanelScale().trim());
				System.out.println("panelScale: " + panelScale);
				if(scale < panelScale)
					scale = panelScale;
			}	
		}
		System.out.println("scale returned: " + scale);
		return scale;
	}
	

}

























