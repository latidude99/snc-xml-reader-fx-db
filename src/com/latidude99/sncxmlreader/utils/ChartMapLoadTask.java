package com.latidude99.sncxmlreader.utils;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;

import com.latidude99.sncxmlreader.model.StandardNavigationChart;
import javafx.concurrent.Task;

public class ChartMapLoadTask extends Task<Map<String, StandardNavigationChart>> {
	Nitrite database;
	List<StandardNavigationChart> chartList;
	Map<String, StandardNavigationChart> chartMap;
	
	
	
	
	public ChartMapLoadTask(Nitrite database) {
		this.database = database;
    }

    @Override
    protected Map<String, StandardNavigationChart> call() throws Exception {
    	chartMap = new TreeMap<>();
    	
    	ObjectRepository<StandardNavigationChart> chartRepository 
    									= database.getRepository(StandardNavigationChart.class);
    	chartList = chartRepository.find().toList();
    	
    	for(StandardNavigationChart chart : chartList)
    		chartMap.put(chart.getShortName(), chart); 
        return chartMap;
    }

    @Override
    protected void failed() {
    	System.out.println("ChartMap NOT loaded from Database: " + chartList.size() + ", charts: ");
    }

    @Override
    protected void succeeded() {
       System.out.println("ChartMap loaded from Database: " + chartList.size() + ", charts: ");
    }
    
   
}

















