package org.crcaguilerapo.ioc;


import dagger.Component;
import org.crcaguilerapo.adapters.in.http.HttpController;
import org.crcaguilerapo.adapters.in.queue.QueueController;
import org.crcaguilerapo.adapters.in.queue.QueueServer;
import org.jooq.DSLContext;

import javax.inject.Singleton;
import java.util.Properties;


@Singleton
@Component(modules = {HttpModule.class, UtilsModule.class, QueueModule.class})
public interface AppComponent {
    HttpController getHttpController();
    QueueController getQueueController();

    QueueServer getQueueServer();

    DSLContext getDatabase();

    Properties getProperties();
}