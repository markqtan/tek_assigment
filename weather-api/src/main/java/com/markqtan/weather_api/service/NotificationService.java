package com.markqtan.weather_api.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SnsClient snsClient;

    public String publishMessage(String topicArn, String message) {
        PublishRequest publishRequest = PublishRequest.builder()
                .topicArn(topicArn)
                .message(message)
                .build();
        PublishResponse response = snsClient.publish(publishRequest);
        return response.messageId();
    }
}