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

import com.latidude99.sncxmlreader.model.StandardNavigationChart;
import javafx.concurrent.Task;
import org.apache.log4j.Logger;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/*
 * Loads charts form database chart repository to a chart Map<String, StandardNavigationChart>
 * (used during application startup) kept in memory untill the app is stopped.
 */
public class ChartMapLoadTask extends Task<Map<String, StandardNavigationChart>> {
    private static final org.apache.log4j.Logger log = Logger.getLogger(ChartMapLoadTask.class);
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
    	log.error("ChartMap NOT loaded from Database: " + chartList.size() + ", charts: ");
    }

    @Override
    protected void succeeded() {
       log.info("ChartMap loaded from Database: " + chartList.size() + ", charts: ");
    }
    
    
   
}

















