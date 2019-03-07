package com.latidude99.sncxmlreader.db;

import org.dizitart.no2.Nitrite;

public class DB {
	private static Nitrite db = null;
	
	private DB() {};
	
	public static Nitrite getNitrite(String dbName) {
		if(db == null) {
			db = Nitrite.builder()
					.compressed()
					.filePath("/" + dbName)
					.openOrCreate();
		}
		return db;
	}

}
