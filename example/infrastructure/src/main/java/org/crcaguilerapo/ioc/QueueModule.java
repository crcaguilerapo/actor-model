package org.crcaguilerapo.ioc;

import com.fasterxml.jackson.databind.ObjectMapper;
import dagger.Module;
import dagger.Provides;
import org.crcaguilerapo.adapters.in.queue.QueueController;
import org.crcaguilerapo.adapters.in.queue.QueueServer;
import org.crcaguilerapo.usecase.PaymentUseCase;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

import javax.inject.Singleton;
import java.net.URI;
import java.util.Properties;

@Module
public class QueueModule {
    @Provides
    @Singleton
    SqsClient sqsClient(Properties properties) {
        var credentials = AwsBasicCredentials.create(
                properties.get("aws.accessKeyId").toString(),
                properties.get("aws.secretAccessKey").toString()
        );
        AwsCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(credentials);

        return SqsClient.builder()
                .credentialsProvider(credentialsProvider)
                .region(Region.of(properties.get("aws.sqs.region").toString()))
                .endpointOverride(URI.create(properties.get("aws.sqs.sqsUrl").toString()))
                .build();
    }

    @Provides
    @Singleton
    QueueController queueController(
            ObjectMapper objectMapper,
            PaymentUseCase paymentUseCase
    ) {
        return new QueueController(objectMapper, paymentUseCase);
    }

    @Provides
    @Singleton
    QueueServer queueServer(
            Properties properties,
            SqsClient sqsClient
    ) {
        return new QueueServer(properties, sqsClient);
    }
}