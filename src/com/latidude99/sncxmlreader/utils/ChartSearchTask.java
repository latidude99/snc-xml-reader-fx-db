package com.latidude99.sncxmlreader.utils;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;
import org.dizitart.no2.objects.filters.ObjectFilters;

import com.latidude99.sncxmlreader.db.Database;
import com.latidude99.sncxmlreader.model.Metadata;
import com.latidude99.sncxmlreader.model.NoticesToMariners;
import com.latidude99.sncxmlreader.model.Panel;
import com.latidude99.sncxmlreader.model.Polygon;
import com.latidude99.sncxmlreader.model.Position;
import com.latidude99.sncxmlreader.model.StandardNavigationChart;
import javafx.concurrent.Task;

public class ChartSearchTask extends Task<String> {
	private Nitrite database;
	private ObjectRepository<StandardNavigationChart> chartRepository;
	private String input;
	private boolean fullInfo;
	
	
	public ChartSearchTask(Nitrite database, String input, boolean fullInfo) {
		this.database = database;
		this.input = input;
		this.fullInfo = fullInfo;
    }

    @Override
    protected String call() throws Exception {
    	System.out.println("ChartSearchTask started");
    	return displayChartRange(input, fullInfo);
    }

    @Override
    protected void failed() {
    	
    }

    @Override
    protected void succeeded() {
       
    }
    
    
    public String displayChartRange(String input, boolean fullInfo) {
		StringBuilder sb = new StringBuilder();
		Set<String> numbersSearched = FormatUtils.parseInput(input);
		System.out.println(numbersSearched);
		Map<String, StandardNavigationChart> chartsFound = findChartsFromRepository(numbersSearched);
        
		sb.append(printSearchSummary(chartsFound, numbersSearched));
		
        if(!chartsFound.keySet().isEmpty()) {
        	for(StandardNavigationChart chart : chartsFound.values()) {
        		if(fullInfo)
        			sb.append(displayChartFullInfo(chart));
				else
					sb.append(displayChartBasicInfo(chart));
        	}
        } else {
			sb.append("\nNo charts have been found"); 
        }
        return sb.toString();
	}
	
	
	
	private Map<String, StandardNavigationChart> findChartsFromRepository(Set<String> numbersSearched){
		System.out.println(database.toString());
		chartRepository = database.getRepository(StandardNavigationChart.class);		
		Map<String, StandardNavigationChart> chartsFound = new TreeMap<>();   
		long count = 0;		
        for(String searchNum : numbersSearched) {
        	StandardNavigationChart chart = null;
        	chart = chartRepository.find(ObjectFilters.eq("shortName", searchNum)).firstOrDefault();
        	count++;
        	long searchedNum = numbersSearched.size();
        	this.updateProgress(count, searchedNum);
        	this.updateMessage("Searched " + count + " charts, found " + chartsFound.size() + " charts");
        	System.out.println("chart: " + chart);
        	System.out.println("count: " + count + ", total searched: " + searchedNum);
        	if(chart != null)
        		chartsFound.put(searchNum, chart);	
        }
        return chartsFound;
	}
	
	private String printSearchSummary(Map<String, StandardNavigationChart> chartsFound, Set<String> numbersSearched) {
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
	
	
	private String displayChartBasicInfo(StandardNavigationChart chart) {
		Metadata meta = chart.getMetadata();
      	StringBuilder sb = new StringBuilder();
      	
      	sb.append(printChartMainInfo(meta));
      	sb.append(printChartPanelsShort(meta));
        return sb.toString();
	}
	
	private String displayChartFullInfo(StandardNavigationChart chart) {
		Metadata meta = chart.getMetadata();
      	StringBuilder sb = new StringBuilder();
      	
      	sb.append(printChartMainInfo(meta));
      	sb.append(printChartPanelsShort(meta));
        sb.append(printChartGeographicLimits(meta));
        sb.append(printChartPanelsLong(meta));
        sb.append(printChartNotices(meta));       
        return sb.toString();
	}
	
	private String printChartMainInfo(Metadata meta) {
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
	
	
	private String printChartGeographicLimits(Metadata meta) {
		StringBuilder sb = new StringBuilder();
		sb.append("");
		if(meta.getGeographicLimit() != null && meta.getGeographicLimit().getPolygons() != null) {
        	sb.append("\n\nGeographic Limits: ");
          	for(Polygon polygon : meta.getGeographicLimit().getPolygons()) {
          		for(Position position : polygon.getPositions())
          			sb.append("\n     Position:     "
          									+ " latitude = " + position.getLatitude()
          									+ " longitude = " + position.getLongitude());
          	}
        }
		return sb.toString();
	}
	
	private String printChartPanelsShort(Metadata meta) {
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
	
	private String printChartPanelsLong(Metadata meta) {
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
          		if(panel.getPolygon() != null && panel.getPolygon().getPositions() != null) {
	          		for(Position position : panel.getPolygon().getPositions()) {
	          			sb.append("\n          Position -- "
	             									+ " latitude = " + position.getLatitude()
	             									+ " longitude = " + position.getLongitude());
	          		}
          		}
          	}	
        }
		return sb.toString();
		
	}
	
	private String printChartNotices(Metadata meta) {
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

















