package com.latidude99.sncxmlreader.db;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;

import com.latidude99.sncxmlreader.model.AppDTO;
import com.latidude99.sncxmlreader.model.BaseFileMetadata;
import com.latidude99.sncxmlreader.model.StandardNavigationChart;
import com.latidude99.sncxmlreader.model.UKHOCatalogueFile;
import javafx.concurrent.Task;

public class DBLoaderTask extends Task<Void> {
	private UKHOCatalogueFile ukhoCatalogueFile;
	private String dbPath;
	
    public DBLoaderTask(UKHOCatalogueFile ukhoCatalogueFile, String dbPath) {
    	this.ukhoCatalogueFile = ukhoCatalogueFile;
    	this.dbPath = dbPath;
    }
	
	@Override
	protected Void call() throws Exception {
		loadChartsInDB();
		return null;
	}
	
	private void loadChartsInDB() {
		Nitrite database = DB.getDB(dbPath);
		
		database.getRepository(StandardNavigationChart.class).drop();
		database.getRepository(BaseFileMetadata.class).drop();
		database.getRepository(AppDTO.class).drop();
		
		ObjectRepository<StandardNavigationChart> chartRepository = database.getRepository(StandardNavigationChart.class);
		ObjectRepository<BaseFileMetadata>metaRepository = database.getRepository(BaseFileMetadata.class);
		ObjectRepository<AppDTO> appDTORepository = database.getRepository(AppDTO.class);
				
		long totalChartsNum = ukhoCatalogueFile.getProducts().getPaper().getCharts().size();
		long loadedChartsNum = 0;
		
		for(StandardNavigationChart chart : ukhoCatalogueFile.getProducts().getPaper().getCharts()) {
			chartRepository.insert(chart);
			loadedChartsNum++;
			this.updateProgress(loadedChartsNum, totalChartsNum);
			this.updateMessage("Loaded:  " + loadedChartsNum + " charts");
			System.out.println(chart.getShortName());
		}
		metaRepository.insert(ukhoCatalogueFile.getBaseFileMetadata());
		AppDTO appDTO = new AppDTO();
		appDTO.setSchemaVersion(ukhoCatalogueFile.getSchemaVersion());
		appDTORepository.insert(appDTO);
	}
}











