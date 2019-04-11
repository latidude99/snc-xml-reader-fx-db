package com.latidude99.sncxmlreader.utils;

public enum ConfigPaths {


    UKHO_HOME("https://enavigator.ukho.gov.uk/"),
    UKHO_LOGIN("https://enavigator.ukho.gov.uk/Login"),
    UKHO_DOWNLOAD("https://enavigator.ukho.gov.uk/Download"),
    FILE_PARAM("?file="),
    PREFERENCES("/sncxmlreader"),
    USER_DB("user.data/db/"),
    USER("user.data/"),
    CONFIG("user.data/config.properties"),
    DATABASE("user.data/db/snc_catalogue.db"),
    XML("user.data/xml/snc_catalogue.xml"),
    HTML("user.data/html/"),
    API_KEY("no default key"),
    APP_VERSION("version 2.3");

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
