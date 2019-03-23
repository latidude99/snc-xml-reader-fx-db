package com.latidude99.sncxmlreader.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;
import org.dizitart.no2.objects.filters.ObjectFilters;

import com.latidude99.sncxmlreader.db.ChartMap;
import com.latidude99.sncxmlreader.db.Database;
import com.latidude99.sncxmlreader.model.StandardNavigationChart;

import javafx.concurrent.Task;

public class ChartSearchTask extends Task<String> {
	private Nitrite database = Database.databaseInstance;
	private ObjectRepository<StandardNavigationChart> chartRepository;
	ChartUtils chartUtils = new ChartUtils();
	private String input;
	private boolean fullInfo;
	long count;	
	long searchedNum;
	
	
	public ChartSearchTask(Nitrite database, String input, boolean fullInfo) {
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
        
		sb.append(chartUtils.printSearchSummary(chartsFound, numbersSearched));
		
        if(!chartsFound.keySet().isEmpty()) {
        	for(StandardNavigationChart chart : chartsFound.values()) {
        		if(fullInfo)
        			sb.append(chartUtils.displayChartFullInfo(chart));
				else
					sb.append(chartUtils.displayChartBasicInfo(chart));
        	}
        } else {
			sb.append("\nNo charts have been found"); 
        }
        return sb.toString();
	}
	
	
	
	private Map<String, StandardNavigationChart> findChartsFromRepository(Set<String> numbersSearched){
//		System.out.println(database.toString());
//		chartRepository = database.getRepository(StandardNavigationChart.class);		
		Map<String, StandardNavigationChart> chartsFound = new TreeMap<>();   
		Map<String, StandardNavigationChart> chartMap = ChartMap.map;
		
		long startTime = System.currentTimeMillis();
		for(String searchNum : numbersSearched) {
			count++;
			searchedNum = numbersSearched.size();
			this.updateProgress(count, searchedNum);
        	this.updateMessage("Searched " + count + " charts, found " + chartsFound.size() + " charts");
//        	System.out.println("chart: " + chart);
        	System.out.println("count: " + count + ", total to search: " + searchedNum);
        	try {
        	    Thread.sleep(2);                
        	} catch(InterruptedException ex) {
        	    Thread.currentThread().interrupt();
        	}
        	for(String chartNum : chartMap.keySet()) {
        		if((searchNum).equals(chartNum))
        				chartsFound.put(chartNum, chartMap.get(chartNum));
        	}	
        }
		ChartMap.found = chartsFound;
		long stopTime = System.currentTimeMillis();
	    long elapsedTime = stopTime - startTime;
	    System.out.println("searched: " + numbersSearched.size() + ", time: " + elapsedTime);		
				
				
/*		
 		// the fastest
 		numbersSearched.parallelStream()
				.forEach(searchNum -> {
					StandardNavigationChart chart = null;
		        	chart = chartRepository.find(ObjectFilters.eq("shortName", searchNum)).firstOrDefault();
		        	count++;
		        	searchedNum = numbersSearched.size();
		        	this.updateProgress(count, searchedNum);
		        	this.updateMessage("Searched " + count + " charts, found " + chartsFound.size() + " charts");
		        	System.out.println("chart: " + chart);
		        	System.out.println("count: " + count + ", total searched: " + searchedNum);
		        	if(chart != null)
		        		chartsFound.put(searchNum, chart);	
		        
				});
		
		// the slowest
		numbersSearched.parallelStream()
				 .filter(c -> 
				 	chartRepository.find(ObjectFilters.eq("shortName", c)).firstOrDefault() != null)
				 .collect(
						 Collectors.toMap(c -> c, c -> chartRepository.find(ObjectFilters.eq("shortName", c)).firstOrDefault()));
						 
		// slower
        for(String searchNum : numbersSearched) {
        	StandardNavigationChart chart = null;
        	chart = chartRepository.find(ObjectFilters.eq("shortName", searchNum)).firstOrDefault();
        	count++;
        	searchedNum = numbersSearched.size();
        	this.updateProgress(count, searchedNum);
        	this.updateMessage("Searched " + count + " charts, found " + chartsFound.size() + " charts");
        	System.out.println("chart: " + chart);
        	System.out.println("count: " + count + ", total searched: " + searchedNum);
        	if(chart != null)
        		chartsFound.put(searchNum, chart);	
        }
        
*/      return chartsFound;
	}
    
	
   
}

















