package org.crcaguilerapo.entrypoints;

import org.crcaguilerapo.ioc.AppComponent;
import org.crcaguilerapo.ioc.DaggerAppComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.crcaguilerapo.infrastructure.Infrastructure.*;


public class InfraApp {

    private static final Logger logger = LoggerFactory.getLogger(InfraApp.class);

    private static final AppComponent appComponent = DaggerAppComponent.create();

    public static void start(AppComponent appComponent) {
        var properties = appComponent.getProperties();
        initDB(properties);
        initSQS(properties);
        var ctx = appComponent.getDatabase();
        seeders(ctx);
    }

    public static void main(String[] args) {
        logger.info("Started infrastructure service...");
        start(appComponent);
    }

}
