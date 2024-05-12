package org.crcaguilerapo.entrypoints;

import org.crcaguilerapo.ioc.AppComponent;
import org.crcaguilerapo.ioc.DaggerAppComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueueApp {
    private static final Logger logger = LoggerFactory.getLogger(QueueApp.class);
    private static final AppComponent appComponent = DaggerAppComponent.create();

    public static void start(AppComponent appComponent) {
        logger.info("Started queue service...");
        appComponent
                .getQueueServer()
                .readMessage(appComponent.getQueueController()::process);
    }
    public static void main(String[] args) {
        start(appComponent);
    }
}
