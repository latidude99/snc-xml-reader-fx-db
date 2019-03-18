package com.latidude99.sncxmlreader.db;

import java.util.List;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;

import com.latidude99.sncxmlreader.model.AppDTO;
import com.latidude99.sncxmlreader.model.BaseFileMetadata;
import com.latidude99.sncxmlreader.model.StandardNavigationChart;
import com.latidude99.sncxmlreader.model.UKHOCatalogueFile;
import javafx.concurrent.Task;

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
		
		
/*		for(StandardNavigationChart chart : ukhoCatalogueFile.getProducts().getPaper().getCharts()) {
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











