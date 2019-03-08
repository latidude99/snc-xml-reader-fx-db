package com.latidude99.sncxmlreader.db;

import org.dizitart.no2.Nitrite;

public class DB {
	private static Nitrite db = null;
	
	private DB() {};
	
	public static Nitrite getDB(String dbPath) {
		if(db == null) {
			db = Nitrite.builder()
				    .compressed()
				    .filePath(dbPath)
				    .openOrCreate("user", "password");
		}
		return db;
	}

}
