package com.markqtan.weather_api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.sqs.SqsClient;

@Component
@RequiredArgsConstructor
public class SqSPublisher {
    private final SqsClient sqsClient;
    @Value("${cloud.aws.sqs.queue}")
    private String queueUrl;

    public void publishMessage(String messageBody) {
        sqsClient.sendMessage(builder -> builder
                .queueUrl(queueUrl)
                .messageBody(messageBody)
                .messageGroupId("default")); // For FIFO queues, a message group ID is required
    }
}
