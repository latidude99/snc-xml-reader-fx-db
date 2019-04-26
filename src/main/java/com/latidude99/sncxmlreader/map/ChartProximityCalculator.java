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
import uk.me.jstott.jcoord.LatLng;

import java.util.*;

/*
 * Calculates what charts should be included in a search result if the search
 * is not an "exact match' type. Looks for charts of smaller scale, larger scale
 * or smaller and larger scale limited by certain conditions (overlapping limits
 * and distance from the centre of the searched chart).
 * FACTOR applied to scale calculations means that SMALLER and LARGER scales values
 * overlap a little bit so that charts of a scale slightly smaller or larger than
 * the searched chart are not excluded from the result.
 */
public class ChartProximityCalculator {
	private static final String SMALLER_SCALE = "-";
	private static final String LARGER_SCALE ="+";
	private static final String RANGE = "range";
	private static final int DISTANCE = 300;
	private static final double FACTOR = 0.9;
	
	ChartCentreCalculator chartCentreCalculator = new ChartCentreCalculator();

	public ChartProximityCalculator() {}

	/*
	 * Although a parameter for the main searched chart (found earlier and passed here)
	 * a Map<String, StandardNavigationChart> is used, only one chart will ever be passed here
	 * at this moment. The use of a Map is meant for future development when more
	 * search types will be introduced.
	 *
	 * @param chartsInputFound - chart searched by a user (and found in the catalogue already)
	 * @param chartsToCheck - all charts in the catalogue
	 * @param searchType - 'range', '_', '+'
	 */
	public Map<String, StandardNavigationChart> totalChartsProximal(Map<String, StandardNavigationChart> chartsInputFound,
																			Map<String, StandardNavigationChart> chartsToCheck,
																			String searchType){
		Map<String, StandardNavigationChart> chartsProximalTotal = new LinkedHashMap<>();
		Map<String, StandardNavigationChart> chartsProximalSingle;
		chartsProximalTotal.putAll(chartsInputFound);
		switch(searchType) {
			case RANGE:
				for(StandardNavigationChart chartInputFound : chartsInputFound.values()) {
					chartsProximalSingle = singleChartRange(chartInputFound, chartsToCheck);
					chartsProximalTotal.putAll(chartsProximalSingle);
				}
				break;
			case SMALLER_SCALE:
				for(StandardNavigationChart chartInputFound : chartsInputFound.values()) {
					chartsProximalSingle = singleChartSmallerScale(chartInputFound, chartsToCheck);
					chartsProximalTotal.putAll(chartsProximalSingle);
				}
				break;
			case LARGER_SCALE:
				for(StandardNavigationChart chartInputFound : chartsInputFound.values()) {
					chartsProximalSingle = singleChartLargerScale(chartInputFound, chartsToCheck);
					chartsProximalTotal.putAll(chartsProximalSingle);
				}
		}
		return chartsProximalTotal;
	}

	/**
	 * Checks for charts with overlapping coverage or with the centre within the DISTANCE to the main chart centre
	 */
	public Map<String, StandardNavigationChart> singleChartRange(StandardNavigationChart chartInputFound, 
														Map<String, StandardNavigationChart> chartsToCheck){
		Map<String, StandardNavigationChart> chartsProximal = new LinkedHashMap<>();		
		for(StandardNavigationChart chartToCheck : chartsToCheck.values()) {
			if(isOverapping(chartInputFound, chartToCheck) 	|| isWithinDistance(DISTANCE, chartInputFound, chartToCheck)) {
				chartsProximal.put(chartToCheck.getShortName(), chartToCheck);
			}
		}
		return chartsProximal;
	}

	/**
	 * Checks for charts with a smaller scale and with the centre within the 5 x DISTANCE to the main chart centre
	 */
	public Map<String, StandardNavigationChart> singleChartSmallerScale(StandardNavigationChart chartInputFound, 
														Map<String, StandardNavigationChart> chartsToCheck){
		Map<String, StandardNavigationChart> chartsProximal = new LinkedHashMap<>();		
		for(StandardNavigationChart chartToCheck : chartsToCheck.values()) {
			if(hasSmallerScale(FACTOR, chartInputFound, chartToCheck) && isWithinDistance(DISTANCE * 5,
					chartInputFound, chartToCheck)) {
				chartsProximal.put(chartToCheck.getShortName(), chartToCheck);
			}
		}
		return chartsProximal;
	}

	/**
	 * Checks for charts with a larger scale and with the centre within the 5 x DISTANCE to the main chart centre
	 */
	public Map<String, StandardNavigationChart> singleChartLargerScale(StandardNavigationChart chartInputFound, 
			Map<String, StandardNavigationChart> chartsToCheck){
		Map<String, StandardNavigationChart> chartsProximal = new LinkedHashMap<>();		
		for(StandardNavigationChart chartToCheck : chartsToCheck.values()) {
			if(hasLargerScale(FACTOR, chartInputFound, chartToCheck) && isWithinDistance(DISTANCE * 5,
					chartInputFound, chartToCheck)) {
				chartsProximal.put(chartToCheck.getShortName(), chartToCheck);
			}
		}
		return chartsProximal;
	}

	// for future development
	private boolean isWithinScale(double factor, StandardNavigationChart chart1, StandardNavigationChart chart2) {
		int scaleChart1 = getMinScale(chart1);
		int scaleChart2 = getMinScale(chart2);
		if((scaleChart1 * factor > scaleChart2) || (scaleChart1 / factor < scaleChart2))
			return true;
		else	
			return false;
	}
	
	private boolean hasSmallerScale(double factor, StandardNavigationChart chart1, StandardNavigationChart chart2) {
		boolean result = false;
		int scaleChart1 = getMinScale(chart1);
		int scaleChart2 = getMinScale(chart2);
		if(scaleChart1 * factor < scaleChart2)
			result = true;
		return result;
	}
	
	private boolean hasLargerScale(double factor, StandardNavigationChart chart1, StandardNavigationChart chart2) {
		boolean result = false;
		int scaleChart1 = getMinScale(chart1);
		int scaleChart2 = getMinScale(chart2);
		if(scaleChart1 > scaleChart2 * factor)
			result = true;
		return result;
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
			return false;
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

	/*
	 * calculates distance between centres of 2 charts (including all panels) in km
 	 */
	private double calcDistance(StandardNavigationChart chart1, StandardNavigationChart chart2) {
		LatLng latlng1 = new LatLng(chartCentreCalculator.calculateChartCentre(chart1).latitude,
									chartCentreCalculator.calculateChartCentre(chart1).longitude);
		LatLng latlng2 = new LatLng(chartCentreCalculator.calculateChartCentre(chart2).latitude,
									chartCentreCalculator.calculateChartCentre(chart2).longitude);
		double distance = latlng1.distance(latlng2);
		return distance;
	}
	
	public int getMinScale(StandardNavigationChart chart) {
		int scale = 0;
		int panelScale = 0;
		
		if(chart.getMetadata().getScale() != null && !"".equals(chart.getMetadata().getScale().trim())) { 
			scale = Integer.parseInt(chart.getMetadata().getScale().trim());
		}
		else if(chart.getMetadata().getPanels() != null) {
			for(Panel panel : chart.getMetadata().getPanels()) {
				if(panel.getPanelScale() !=null && !panel.getPanelScale().trim().equals("")) {
					panelScale = Integer.parseInt(panel.getPanelScale().trim());
					if(scale < panelScale)
						scale = panelScale;
				}	
			}	
		}
		return scale;
	}
	

}

























