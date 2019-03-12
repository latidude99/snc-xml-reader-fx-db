package com.latidude99.sncxmlreader.db;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;

import com.latidude99.sncxmlreader.model.AppDTO;
import com.latidude99.sncxmlreader.model.BaseFileMetadata;
import com.latidude99.sncxmlreader.model.StandardNavigationChart;
import com.latidude99.sncxmlreader.model.UKHOCatalogueFile;
import com.latidude99.sncxmlreader.utils.FileLoadTask;
import com.latidude99.sncxmlreader.utils.MessageBox;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

public class DBLoader {
	
	public static void loadDBFromFile(String filePath) {	
		Nitrite database = DB.getDB(filePath);
		ObjectRepository<StandardNavigationChart> chartRepository = database.getRepository(StandardNavigationChart.class);
		ObjectRepository<BaseFileMetadata>metaRepository = database.getRepository(BaseFileMetadata.class);
		ObjectRepository<AppDTO> appDTORepository = database.getRepository(AppDTO.class);
		Task<UKHOCatalogueFile> fileLoadTask = new FileLoadTask(filePath);
		
		fileLoadTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, 
				new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent t) {					
				for(StandardNavigationChart chart : fileLoadTask.getValue().getProducts().getPaper().getCharts()) {
					chartRepository.insert(chart);
				}
				metaRepository.insert(fileLoadTask.getValue().getBaseFileMetadata());
				AppDTO appDTO = new AppDTO();
				appDTO.setSchemaVersion(fileLoadTask.getValue().getSchemaVersion());
				appDTORepository.insert(appDTO);
				MessageBox.show("Catalogue has been successfully loaded into Database", "Info");
				}
			});
		fileLoadTask.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED, 
							new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent t) {
				MessageBox.show("Catalogue file not found (snc_catalogue.xml). Please load the file manually \n"
						+ "or download the latest catalogue file from UKHO website.", "Error");
				}
			});
		
		Thread thread = new Thread(fileLoadTask);
        thread.setDaemon(true);
        thread.start();
	}
	
}














