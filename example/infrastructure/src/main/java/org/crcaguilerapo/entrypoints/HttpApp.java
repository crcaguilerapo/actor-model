package org.crcaguilerapo.entrypoints;

import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.ServerBuilder;
import org.crcaguilerapo.adapters.in.http.HttpController;
import org.crcaguilerapo.ioc.AppComponent;
import org.crcaguilerapo.ioc.DaggerAppComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpApp {
    private static final Logger logger = LoggerFactory.getLogger(HttpApp.class);
    private static final AppComponent appComponent = DaggerAppComponent.create();

    static Server newServer(int port, HttpController controller) {
        ServerBuilder sb = Server.builder();
        return sb
                .http(port)
                .annotatedService(controller)
                .build();
    }

    public static void start(AppComponent appComponent) {
        logger.info("Started http service...");
        Server server = newServer(8080, appComponent.getHttpController());
        server.closeOnJvmShutdown();
        server.start().join();
    }

    public static void main(String[] args) {
        start(appComponent);
    }
}