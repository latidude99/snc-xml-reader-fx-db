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

import com.latidude99.sncxmlreader.model.StandardNavigationChart;

import java.util.LinkedHashMap;
import java.util.Map;

/*
 * This class static fields serve as temporary (when the application
 * is running) global variables holding data. This is much faster than
 * reading data form database. Could not find the way to sufficiently
 * speed up database operations, even with parallel streams processing,
 * especially searching. The application memory footprint has not increased
 * significantly but searching speed increased two or sometimes three-fold.
 */
public class ChartMap {
	
	public static Map<String, StandardNavigationChart> display = new LinkedHashMap<>();
	
	public static Map<String, StandardNavigationChart> all = new LinkedHashMap<>();
	
	public static Map<String, StandardNavigationChart> found = new LinkedHashMap<>();
	
	public static Map<String, StandardNavigationChart> proximal = new LinkedHashMap<>();
	
	
	
}
