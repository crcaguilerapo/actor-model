package org.crcaguilerapo.entrypoints;

import org.crcaguilerapo.ioc.AppComponent;
import org.crcaguilerapo.ioc.DaggerAppComponent;

public class App {
    private static final AppComponent appComponent = DaggerAppComponent.create();

    public static void main(String[] args) {
        HttpApp.start(appComponent);
        QueueApp.start(appComponent);
    }
}
