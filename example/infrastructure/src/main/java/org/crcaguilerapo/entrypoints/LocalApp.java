package org.crcaguilerapo.entrypoints;

import org.crcaguilerapo.ioc.AppComponent;
import org.crcaguilerapo.ioc.DaggerAppComponent;

public class LocalApp {
    private static final AppComponent appComponent = DaggerAppComponent.create();

    public static void main(String[] args) {
        InfraApp.start(appComponent);
        HttpApp.start(appComponent);
        QueueApp.start(appComponent);
    }
}
