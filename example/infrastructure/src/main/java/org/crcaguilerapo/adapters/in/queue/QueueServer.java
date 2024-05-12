package org.crcaguilerapo.adapters.in.queue;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

import java.util.List;
import java.util.Properties;
import java.util.function.Function;

public class QueueServer {
    private final Properties properties;
    private final SqsClient sqsClient;

    public QueueServer(Properties properties, SqsClient sqsClient) {
        this.properties = properties;
        this.sqsClient = sqsClient;
    }

    private void listen(ReceiveMessageRequest receiveMessageRequest, Function<Message, Message> func) {
        while (true) {
            List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).messages();
            for (Message message : messages) {
                Thread.Builder builder = Thread.ofVirtual();
                builder.start(() -> {
                    Message m = func.apply(message);
                    sqsClient.deleteMessage(
                            DeleteMessageRequest.builder()
                            .queueUrl(properties.get("aws.sqs.queueUrl").toString())
                            .receiptHandle(m.receiptHandle())
                            .build()
                    );
                });
            }
        }
    }

    public void readMessage(Function<Message, Message> func) {
        ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                .queueUrl(properties.get("aws.sqs.queueUrl").toString())
                .maxNumberOfMessages(Integer.parseInt(properties.get("aws.sqs.maxNumberOfMessages").toString()))
                .waitTimeSeconds(Integer.parseInt(properties.get("aws.sqs.waitTimeSeconds").toString()))
                .visibilityTimeout(Integer.parseInt(properties.get("aws.sqs.visibilityTimeout").toString()))
                .build();

        Thread.Builder builder = Thread.ofVirtual().name("queue");
        Thread t = builder.start(() -> listen(receiveMessageRequest, func));
        try {
            t.join();
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }
}
