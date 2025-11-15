package com.markqtan.weather_api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.sqs.SqsClient;

@Component
@RequiredArgsConstructor
public class SqSPublisher {
    @Value("${cloud.aws.sqs.queue}")
    private String queueUrl;

    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;

    public void publishMessage(Object messageBody) throws JsonProcessingException {
        publishMessage(objectMapper.writeValueAsString(messageBody));
    }

    public void publishMessage(String messageBody) {
        sqsClient.sendMessage(builder -> builder
                .queueUrl(queueUrl)
                .messageBody(messageBody)
                .messageGroupId("default")); // For FIFO queues, a message group ID is required
    }
}
