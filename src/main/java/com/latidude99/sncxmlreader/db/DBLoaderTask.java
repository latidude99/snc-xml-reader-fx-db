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

package com.latidude99.sncxmlreader.db;

import com.latidude99.sncxmlreader.model.AppDTO;
import com.latidude99.sncxmlreader.model.BaseFileMetadata;
import com.latidude99.sncxmlreader.model.StandardNavigationChart;
import com.latidude99.sncxmlreader.model.UKHOCatalogueFile;
import javafx.concurrent.Task;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;

import java.util.List;

public class DBLoaderTask extends Task<Nitrite> {
	private UKHOCatalogueFile ukhoCatalogueFile;
	private String dbPath;
	long loadedChartsNum = 0;
		
    public DBLoaderTask(UKHOCatalogueFile ukhoCatalogueFile, String dbPath) {
    	this.ukhoCatalogueFile = ukhoCatalogueFile;
    	this.dbPath = dbPath;
    }
	
	@Override
	protected Nitrite call() throws Exception {
		Nitrite database = loadChartsInDB();
		return database;
	}
	
	private Nitrite loadChartsInDB() {
		Nitrite database = Nitrite.builder()
			    .compressed()
			    .filePath(dbPath)
			    .openOrCreate("user", "password");
		System.out.println("loadChartsInDB: " + database.getContext().toString());
		Database.databaseInstance = database;
		ObjectRepository<StandardNavigationChart> chartRepository = database.getRepository(StandardNavigationChart.class);
		ObjectRepository<BaseFileMetadata>metaRepository = database.getRepository(BaseFileMetadata.class);
		ObjectRepository<AppDTO> appDTORepository = database.getRepository(AppDTO.class);
				
		long totalChartsNum = ukhoCatalogueFile.getProducts().getPaper().getCharts().size();
		
		List<StandardNavigationChart> charts = ukhoCatalogueFile.getProducts().getPaper().getCharts();
		charts.parallelStream()
				.forEach(c -> {chartRepository.insert(c);
								this.loadedChartsNum++;
								this.updateProgress(loadedChartsNum, totalChartsNum);
								this.updateMessage("Loaded:  " + loadedChartsNum + " of " + totalChartsNum + " charts");
								System.out.println(c.getShortName());
				});
		
/*		
		for(StandardNavigationChart chart : ukhoCatalogueFile.getProducts().getPaper().getCharts()) {
			 if (this.isCancelled()) {
				 this.updateMessage("Loading database stopped, loaded:  " + loadedChartsNum + " of " + totalChartsNum + " charts");
	             break;
			 }
			chartRepository.insert(chart);
			loadedChartsNum++;
			this.updateProgress(loadedChartsNum, totalChartsNum);
			this.updateMessage("Loaded:  " + loadedChartsNum + " of " + totalChartsNum + " charts");
			System.out.println(chart.getShortName());
		}
*/
		metaRepository.insert(ukhoCatalogueFile.getBaseFileMetadata());
		AppDTO appDTO = new AppDTO();
		appDTO.setSchemaVersion(ukhoCatalogueFile.getSchemaVersion());
		appDTORepository.insert(appDTO);
		
		return database;
	}
}











