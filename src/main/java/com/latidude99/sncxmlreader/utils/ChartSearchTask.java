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

package com.latidude99.sncxmlreader.utils;

import com.latidude99.sncxmlreader.db.ChartMap;
import com.latidude99.sncxmlreader.db.Database;
import com.latidude99.sncxmlreader.map.ChartProximityCalculator;
import com.latidude99.sncxmlreader.model.StandardNavigationChart;
import javafx.concurrent.Task;
import org.apache.log4j.Logger;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ChartSearchTask extends Task<String> {
	private static final org.apache.log4j.Logger log = Logger.getLogger(ChartSearchTask.class);
	private static final String SINGLE = "=";
	private static final String SMALLER_SCALE = "-";
	private static final String LARGER_SCALE ="+";
	private static final String RANGE = "range";
	private Nitrite database = Database.databaseInstance;
	private ObjectRepository<StandardNavigationChart> chartRepository;
	ChartUtils chartUtils = new ChartUtils();
	private String input;
	private boolean fullInfo;
	private String searchType;
	long count;	
	long searchedNum;

	public ChartSearchTask(Nitrite database){
		this.database = database;
	}
	
	
	public ChartSearchTask(String input, boolean fullInfo) {
		this.input = input;
		this.fullInfo = fullInfo;
	}

	public ChartSearchTask(Nitrite database, String input, boolean fullInfo, String searchType) {
		this.input = input;
		this.fullInfo = fullInfo;
		this.searchType = searchType;
    }


    @Override
    protected String call() {
    	System.out.println("ChartSearchTask started");
    	return displayChartRange(input, fullInfo);
    }

    @Override
    protected void failed() {
		log.error("Chart search task failed");
    }

    @Override
    protected void succeeded() {
       log.info("Chart search task finished successfully");
    }
    
    public String displayChartRange(String input, boolean fullInfo) {
		StringBuilder sb = new StringBuilder();
		Set<String> numbersSearched = FormatUtils.parseInput(input);
		System.out.println(numbersSearched);
		Map<String, StandardNavigationChart> chartsFound = findChartsFromRepository(numbersSearched, searchType);
        
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

	/*
	 * Thread put to sleep during the search to visualize progress,
	 * otherwise too quick to notice when small number of charts
	 * is being searched for (which is the usual usage scenario).
	 */
	public Map<String, StandardNavigationChart> findChartsFromRepository(Set<String> numbersSearched, String searchType){
		ChartProximityCalculator chartProximityCalculator = new ChartProximityCalculator();
		Map<String, StandardNavigationChart> chartsFound = new LinkedHashMap<>();
		Map<String, StandardNavigationChart> chartsProximal = new LinkedHashMap<>();
		Map<String, StandardNavigationChart> chartMap = ChartMap.all;
		
		long startTime = System.currentTimeMillis();
		for(String searchNum : numbersSearched) {
			count++;
			searchedNum = numbersSearched.size();
			this.updateProgress(count, searchedNum);
        	this.updateMessage("Searched " + count + " charts, found " + chartsFound.size() + " charts");
        	try {
        	    Thread.sleep(1);                
        	} catch(InterruptedException ex) {
        	    Thread.currentThread().interrupt();
        	}
        	for(String chartNum : chartMap.keySet()) {
        		if((searchNum).equals(chartNum))
        				chartsFound.put(chartNum, chartMap.get(chartNum));
        	}	
        }

		// If more than one chart is found (implies that more than one chart was searched for)
		// then no proximity calculations are conducted. That is due to the fact that if
		// a user looks for multiple charts then they are not interested in other charts
		// but exactly the numbers they searched for. This approach may be altered in the future.
		ChartMap.found = chartsFound;
		if(chartsFound.size() > 1) {
			ChartMap.display = chartsFound;
		}else if(SINGLE.equals(searchType)) {
			ChartMap.display = chartsFound;
		}else if(SMALLER_SCALE.equals(searchType)) {
			chartsProximal = chartProximityCalculator.totalChartsProximal(chartsFound, ChartMap.all, SMALLER_SCALE);
			ChartMap.display = chartsProximal;
		}else if(LARGER_SCALE.equals(searchType)) {
			chartsProximal = chartProximityCalculator.totalChartsProximal(chartsFound, ChartMap.all, LARGER_SCALE);
			ChartMap.display = chartsProximal;
		}else if(RANGE.equals(searchType)){
			chartsProximal = chartProximityCalculator.totalChartsProximal(chartsFound, ChartMap.all, RANGE);
			ChartMap.display = chartsProximal;
		}
			
		long stopTime = System.currentTimeMillis();
	    long elapsedTime = stopTime - startTime;
	    System.out.println("searched: " + numbersSearched.size() + ", time: " + elapsedTime);		
		
	    return ChartMap.display;
	}

}
				

    
	
   

















