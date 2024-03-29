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

import org.dizitart.no2.Nitrite;

public class Database {
	public static Nitrite databaseInstance;
	
	private Database() {};

	/*
	 * Reads database from file or creates a new one if no file is found.
	 */
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
