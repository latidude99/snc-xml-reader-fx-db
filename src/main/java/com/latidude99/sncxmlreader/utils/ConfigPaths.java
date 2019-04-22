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

package com.latidude99.sncxmlreader.utils;

public enum ConfigPaths {


    UKHO_HOME("https://enavigator.ukho.gov.uk/"),
    UKHO_LOGIN("https://enavigator.ukho.gov.uk/Login"),
    UKHO_DOWNLOAD("https://enavigator.ukho.gov.uk/Download"),
    FILE_PARAM("?file="),
    PREFERENCES("/sncxmlreader"),
    USER_DB("user.data/db/"),
    USER_XML("user.data/xml/"),
    USER_XML_FOLDER("user.data/xml"),
    USER_DB_FOLDER("user.data/db"),
    USER("user.data/"),
    CONFIG("user.data/config.properties"),
    DATABASE("user.data/db/snc_catalogue.db"),
    XML("user.data/xml/snc_catalogue.xml"),
    HTML("user.data/html/"),
    TXT("user.data/txt/"),
    API_KEY("no default key"),
    APP_VERSION("version 2.3"),
    XML_FILE_PROPERTY("xml_file_path"),
    DB_FILE_PROPERTY("db_file_path"),
    API_KEY_PROPERTY("api_key");


    private String path;

    private ConfigPaths(String path) {
        this.path = path;
    }

    public String getPath(){
        return path;
    }

    public void setPath(String path){
        this.path = path;
    }
}
