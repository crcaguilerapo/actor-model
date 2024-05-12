package org.crcaguilerapo.infrastructure;

import org.jooq.DSLContext;
import org.jooq.impl.SQLDataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.Container;
import org.testcontainers.containers.GenericContainer;

import java.io.IOException;
import java.util.Properties;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

public class Infrastructure {

    private static final Logger logger = LoggerFactory.getLogger(Infrastructure.class);

    public static void seeders(DSLContext ctx) {

        var table = table("ACCOUNT");
        var numberAccount = field("NUMBER_ACCOUNT", SQLDataType.INTEGER.nullable(false).identity(true));
        var balance = field("BALANCE", SQLDataType.INTEGER);

        ctx
                .createTable(table)
                .column(numberAccount)
                .column(balance)
                .execute();


        ctx
                .insertInto(table, numberAccount, balance)
                .values(1, 200)
                .values(2, 500)
                .execute();
    }

    public static void initDB(Properties properties) {
        GenericContainer postgres = new GenericContainer("postgres:16.3")
                .withExposedPorts(5432)
                .withEnv("POSTGRES_DB", properties.getProperty("postgres.database"))
                .withEnv("POSTGRES_USER", properties.getProperty("postgres.user"))
                .withEnv("POSTGRES_PASSWORD", (String) properties.get("postgres.password"));

        postgres.start();

        var url = "jdbc:postgresql://$HOST:$PORT/$DB"
                .replace("$HOST", postgres.getHost())
                .replace("$PORT", postgres.getFirstMappedPort().toString())
                .replace("$DB", properties.getProperty("postgres.database"));

        properties.setProperty("postgres.url", url);
    }

    public static void initSQS(Properties properties) {
        var sqsName = properties.getProperty("aws.sqs.name");
        var sqsRegion = properties.getProperty("aws.sqs.region");
        GenericContainer sqs = new GenericContainer("localstack/localstack:3.3")
                .withExposedPorts(4566);
        sqs.start();
        var command = """
                awslocal sqs create-queue --queue-name $SQS_NAME --region $REGION
                """;
        try {
            Container.ExecResult result = sqs.execInContainer(
                    "sh",
                    "-c",
                    command
                            .replace("$SQS_NAME", sqsName)
                            .replace("$REGION", sqsRegion)
            );
            String output = result.getStdout();
            logger.info(output);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        var url = "http://$HOST:$PORT"
                .replace("$HOST", sqs.getHost())
                .replace("$PORT", sqs.getFirstMappedPort().toString());
        properties.setProperty("aws.sqs.sqsUrl", url);
        properties.setProperty("aws.sqs.queueUrl", url + "/000000000000/$SQS_NAME".replace("$SQS_NAME", sqsName));
    }
}
