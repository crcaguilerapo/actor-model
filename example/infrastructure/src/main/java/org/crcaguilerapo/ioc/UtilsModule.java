package org.crcaguilerapo.ioc;

import com.fasterxml.jackson.databind.ObjectMapper;
import dagger.Module;
import dagger.Provides;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@Module
public class UtilsModule {

    @Provides
    @Singleton
    Properties loadProperties() {
        Properties props = new Properties();
        try {
            InputStream in = getClass().getResourceAsStream("/properties");
            props.load(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return props;
    }


    @Provides
    @Singleton
    ObjectMapper objectMapper () {
        return new ObjectMapper();
    }

    @Provides
    @Singleton
    DSLContext ctx(Properties properties) {
        try {
            Connection conn = DriverManager
                    .getConnection(
                            properties.get("postgres.url").toString(),
                            properties.get("postgres.user").toString(),
                            properties.get("postgres.password").toString()
                    );
            return  DSL.using(conn, SQLDialect.POSTGRES);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}