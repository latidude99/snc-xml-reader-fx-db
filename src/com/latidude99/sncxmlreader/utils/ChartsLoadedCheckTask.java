package com.latidude99.sncxmlreader.utils;

import com.latidude99.sncxmlreader.db.ChartMap;

import javafx.concurrent.Task;
// not used now
public class ChartsLoadedCheckTask extends Task<Boolean> {
	
	public ChartsLoadedCheckTask() {
    }

    @Override
    protected Boolean call() throws Exception {
    	boolean chartsLoaded = false;
    	while(!chartsLoaded) {
    		if(ChartMap.all != null && ChartMap.all.size() > 3800 )
    			chartsLoaded = true;
    		Thread.sleep(3000);
    	}	
        return chartsLoaded;
    }

    @Override
    protected void failed() {
    	System.out.println("ChartMap NOT loaded from Database: " + ChartMap.all.size() + ", charts: ");
    }

    @Override
    protected void succeeded() {
       System.out.println("ChartMap loaded from Database: " + ChartMap.all.size() + ", charts: ");
    }
    
    
   
}

















