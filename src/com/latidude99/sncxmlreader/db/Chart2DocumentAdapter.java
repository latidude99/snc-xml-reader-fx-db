package com.latidude99.sncxmlreader.db;

import org.dizitart.no2.Document;
import com.latidude99.sncxmlreader.model.StandardNavigationChart;

public class Chart2DocumentAdapter{
	
	public static Document chart2Doc(StandardNavigationChart chart) {
		
		Document doc = Document.createDocument("chartNumber", chart.getMetadata().getCatalogueNumber())
						.put("chartInternationalNumber", chart.getMetadata().getChartInternationalNumber())
						.put("datasetTitle", chart.getMetadata().getDatasetTitle())
						.put("chartScale", chart.getMetadata().getScale())
						.put("chartStatus", chart.getMetadata().getStatus().getChartStatus().getValue())
						.put("date",chart.getMetadata().getStatus().getChartStatus().getDate())
						.put("geographicLimits", chart.getMetadata().getGeographicLimit().getPolygons())
						.put("notices", chart.getMetadata().getNotices())
						.put("panels", chart.getMetadata().getPanels());					
		return doc;
	}
}

/*
 		ArrayList<String> latlonsMain = new ArrayList<>();
		String latlon = "";
		if(chart.getMetadata().getGeographicLimit() != null 
				&& chart.getMetadata().getGeographicLimit().getPolygons() != null){
			for(Position position : chart.getMetadata().getGeographicLimit().getPolygons().get(0).getPositions()){
				latlon = "Latitude: "+ position.getLatitude() + ", Longitude: " + position.getLongitude();
				latlonsMain.add(latlon);
			}
		}
		ArrayList<String> notices = new ArrayList<>();
		String notice = "";
		if(chart.getMetadata().getNotices() != null) {	
			for(NoticesToMariners n : chart.getMetadata().getNotices()) {
				notice = n.toString();
				notices.add(notice);
			}
		}
 */













