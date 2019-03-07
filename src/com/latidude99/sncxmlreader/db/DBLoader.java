package com.latidude99.sncxmlreader.db;

import org.dizitart.no2.Document;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.NitriteCollection;

import com.latidude99.sncxmlreader.model.StandardNavigationChart;
import com.latidude99.sncxmlreader.utils.ChartUtils;
import com.latidude99.sncxmlreader.utils.FileLoadTask;
import com.latidude99.sncxmlreader.utils.Info;
import com.latidude99.sncxmlreader.utils.LoadTask;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

public class DBLoader {
	Task<Void> fileLoadTask = new FileLoadTask(file);

	public void loadFile2DB(String file) {
		fileLoadTask = new FileLoadTask(file);
		fileLoadTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, 
				new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent t) {
				ukhoCatalogueFile = ChartUtils.getUkhoCatalogueFile();
				schemaVersion = ukhoCatalogueFile.getSchemaVersion();
				meta = ukhoCatalogueFile.getBaseFileMetadata();
				textResult.setText(Info.catalogueFull(meta, schemaVersion));
				labelLoadedDate.setText(Info.catalogueBasic(meta, schemaVersion));
				setDateLabels(meta);
				standardCharts = ChartUtils.getCharts();
				ukhoCatalogueFile = null;
				//MessageBox.show("Catalogue loaded successfully.", "Info");
			}
		});
		loadTask.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED, 
						new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent t) {
				textResult.setText("Catalogue not loaded. Please load the file manually \n"
				+ "or \n"
				+ "copy the catalogue file into the application folder and start SncXmlReader again\n"
				+ "or \n"
				+ "download the latest catalogue file from UKHO website.");
			}
		});

		Thread thread = new Thread(loadTask);
		thread.setDaemon(true);
		thread.start();
	}
	
	public Nitrite db = DB.getNitrite("snc");
	
	public NitriteCollection chartCollection = db.getCollection("charts");
	
	for(StandardNavigationChart chart : ChartUtils.getUkhoCatalogueFile()) {
		Document doc = Chart2DocumentAdapter.chart2Doc(chart);
		chartCollection.insert(doc);
	}
	
	
		

}
