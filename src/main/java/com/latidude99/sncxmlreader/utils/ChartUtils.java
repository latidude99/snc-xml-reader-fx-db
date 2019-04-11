package com.latidude99.sncxmlreader.utils;

import java.util.Map;
import java.util.Set;

import com.latidude99.sncxmlreader.model.Metadata;
import com.latidude99.sncxmlreader.model.NoticesToMariners;
import com.latidude99.sncxmlreader.model.Panel;
import com.latidude99.sncxmlreader.model.Polygon;
import com.latidude99.sncxmlreader.model.Position;
import com.latidude99.sncxmlreader.model.StandardNavigationChart;

public class ChartUtils {
	
	public String printSearchSummary(Map<String, StandardNavigationChart> chartsFound, Set<String> numbersSearched) {
		StringBuilder sb = new StringBuilder();
        String chartsEntered = " chart";
        String chartsListed = " chart";
        if(numbersSearched.size() > 1)
        	chartsEntered = " charts";
        if(chartsFound.keySet().size() > 1)
        	chartsListed = " charts";
        sb.append("Searched for " + numbersSearched.size() + chartsEntered + "\n");
        sb.append(FormatUtils.printSet20Cols(numbersSearched));
        sb.append("\n");
        sb.append("\nFound " + chartsFound.keySet().size() + chartsListed + "\n");
        sb.append(FormatUtils.printSet20Cols(chartsFound.keySet()));
        sb.append("\n-------------------------------------------------");
        return sb.toString();
	}
	
	
	public String displayChartBasicInfo(StandardNavigationChart chart) {
		Metadata meta = chart.getMetadata();
      	StringBuilder sb = new StringBuilder();
      	
      	sb.append(printChartMainInfo(meta));
      	sb.append(printChartPanelsShort(meta));
        return sb.toString();
	}
	
	public String displayChartFullInfo(StandardNavigationChart chart) {
		Metadata meta = chart.getMetadata();
      	StringBuilder sb = new StringBuilder();
      	
      	sb.append(printChartMainInfo(meta));
      	sb.append(printChartPanelsShort(meta));
        sb.append(printChartGeographicLimits(meta));
        sb.append(printChartPanelsLong(meta));
        sb.append(printChartNotices(meta));       
        return sb.toString();
	}
	
	public String printChartMainInfo(Metadata meta) {
		String international = "";
		StringBuilder sb = new StringBuilder();
		sb.append("\n\nChart Number: " + meta.getCatalogueNumber());
		if(meta.getChartInternationalNumber() != null)
			international = meta.getChartInternationalNumber();
		sb.append("\nChart International Number: " + international); 
		if((meta.getScale()) != null) {
			int scaleNumber = Integer.parseInt(meta.getScale());
			String scaleFormatted = String.format("%,d", scaleNumber);
			sb.append("\nChart Scale: 1:" + scaleFormatted);
		}
		sb.append("\nChart Title: " + meta.getDatasetTitle());
		sb.append("\nChart Status: " + meta.getStatus().getChartStatus().getValue()
							+ ", Date: " + meta.getStatus().getChartStatus().getDate());
		return sb.toString();
	}
	
	
	public String printChartGeographicLimits(Metadata meta) {
		StringBuilder sb = new StringBuilder();
		sb.append("");
		if(meta.getGeographicLimit() != null && meta.getGeographicLimit().getPolygons() != null) {
        	sb.append("\n\nGeographic Limits: ");
          	for(Polygon polygon : meta.getGeographicLimit().getPolygons()) {
				sb.append("\r\n               Polygon: ");
          		for(Position position : polygon.getPositions())
					sb.append("\n                          Position:     "
          									+ " latitude = " + position.getLatitude()
          									+ " longitude = " + position.getLongitude());
          	}
        }
		return sb.toString();
	}
	
	public String printChartPanelsShort(Metadata meta) {
		StringBuilder sb = new StringBuilder();
		String additionalPanels = "none";
      	if(meta.getPanels() != null && meta.getPanels().size() > 1) {
      		additionalPanels = "";
      		for(Panel panel : meta.getPanels()) {
      			additionalPanels = additionalPanels + "\n" + 
      					"                         Panel " + panel.getPanelID() + 
      					" scale: 1:" + panel.getPanelScale() + 
      					" (" + panel.getPanelAreaName() + ")";
      		}
      	}
      	sb.append("\n\nAdditional panels: " + additionalPanels + "\n");
        sb.append("\n-------------------------------------------------");
        
        return sb.toString();
	}
	
	public String printChartPanelsLong(Metadata meta) {
		StringBuilder sb = new StringBuilder();
		sb.append("");
		if(meta.getPanels() != null && meta.getPanels().size() >1) {
        	sb.append("\n\nPanels: ");
          	for(Panel panel : meta.getPanels()) {
          		sb.append("\n     Panel Area Name: " + panel.getPanelAreaName());
          		sb.append("\n     Panel ID: " + panel.getPanelID());
          		sb.append("\n     Panel Name: " + panel.getPanelName());
          		int scaleNumber = Integer.parseInt(panel.getPanelScale());
          		String scaleFormatted = String.format("%,d", scaleNumber);
          		sb.append("\n     Panel Scale: " + scaleFormatted);
				if (panel.getPolygons() != null) {
					for (Polygon polygon : panel.getPolygons()) {
						sb.append("\r\n               Polygon: ");
						for (Position position : polygon.getPositions()) {
							sb.append("\n                          " + " latitude = " + position.getLatitude()
									+ " longitude = " + position.getLongitude());
						}
					}

          		}
          	}	
        }
		return sb.toString();
		
	}
	
	public String printChartNotices(Metadata meta) {
		StringBuilder sb = new StringBuilder();
		sb.append("");
		if(meta.getNotices() != null) {
        	sb.append("\n\nNotices To Mariners: ");
          	for(NoticesToMariners notice : meta.getNotices()) {
          		sb.append("\n" + notice);
          	}
        } else {
        	sb.append("\n\nThere is no NMs for this chart yet.");
        }
        sb.append("\n=================================================");
        return sb.toString();
	}
}










