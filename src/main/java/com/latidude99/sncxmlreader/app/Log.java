package com.latidude99.sncxmlreader.app;

import org.apache.log4j.*;

import java.io.IOException;


public class Log {
    private static final Logger log = Logger.getLogger(Log.class);

    public static void log4jConfig(){

        Logger rootLogger = Logger.getRootLogger();
        rootLogger.setLevel(Level.INFO);

        PatternLayout layout = new PatternLayout("%d{ISO8601} [%t] %-5p %c %x - %m%n");

        rootLogger.addAppender(new ConsoleAppender(layout));

        try {
            RollingFileAppender fileAppender = new RollingFileAppender(layout, "user.data/log/sncxmlreader.log");
            rootLogger.addAppender(fileAppender);
        }
        catch (IOException e) {
                System.out.println("Failed to add appender !!");
        }

        // Log messages check
        log.info("Logging level: [----- Log INFO -----]");
        log.debug("[Logging level: ----- Log DEBUG -----]");
        log.error("[Logging level: ----- LOG ERROR -----]");
        log.fatal("[Logging level: ----- LOG FATAL -----]");

    }

}
