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
