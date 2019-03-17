package com.latidude99.sncxmlreader.db;

import org.dizitart.no2.Nitrite;

public class Database {
	public static Nitrite databaseInstance;
	
	private Database() {};
	
	public static Nitrite getDatabaseInstance(String dbPath) {
		if(databaseInstance == null) {
			databaseInstance = Nitrite.builder()
				    .compressed()
				    .filePath(dbPath)
				    .openOrCreate("user", "password");
		}
		return databaseInstance;
	}

}
