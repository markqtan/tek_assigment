package com.markqtan.weather_api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.Notification;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
// import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;

@Component
@Slf4j
@RequiredArgsConstructor
public class SqsMessageListener {

    @Value("${cloud.aws.sqs.queue}")
    private String queueUrl;
    @Value("${cloud.aws.s3.notification.queue}")
    private String notificationQueueUrl;

    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;
    private final StorageService storageService;

    @Scheduled(fixedDelay = 5000)
    public void processS3Notifications() throws JsonMappingException, JsonProcessingException {
        // receive messages from queue
        ReceiveMessageResponse receiveMessageResponse = retrieveMessages(notificationQueueUrl, 10);

        // transform notifications
        List<Message> messages = receiveMessageResponse.messages();
        for (Message message : messages) {
            String body = message.body();
            log.info("processing s3 notifiction Event {} ", body);
            Map<String, List<Map<String, Object>>> sqsEvent = objectMapper.readValue(body, HashMap.class); 
            Map<String, Object> record = sqsEvent.get("Records").get(0);    
            Map<String, Object> s3 = (Map<String, Object>) record.get("s3");
            Map<String, Object> object = (Map<String, Object>) s3.get("object");
            String key = (String) object.get("key");
            log.info("key: {}", key);
            String w = (String) storageService.getObject(key);
            log.info("retrieved object from s3: {}", w);
            sqsClient.deleteMessage(builder -> {
                builder.queueUrl(notificationQueueUrl).receiptHandle(message.receiptHandle());
            });
        }
    }

    @Scheduled(fixedDelay = 5000)
    public void processNotifications() {
        // receive messages from queue
        ReceiveMessageResponse receiveMessageResponse = retrieveMessages(queueUrl, 10);

        // transform notifications
        List<Message> messages = receiveMessageResponse.messages();
        List<Notification> notificationsToSend = new ArrayList<>(messages.size());
        List<String> notificationReceipts = new ArrayList<>(messages.size());
        for (Message message : messages) {
            String body = message.body();

            log.info("processing sqsEvent {} ", body);
            try {
                // extract SNS event
                // HashMap snsEvent = objectMapper.readValue(body, HashMap.class);

                // Notification is expected to be wrapped in the SNS message body
                // String notificationString = snsEvent.get("Message").toString();

                // Notification notification = objectMapper.readValue(notificationString,
                // Notification.class);
                // notificationsToSend.add(notification);
                // notificationReceipts.add(message.receiptHandle());

                sqsClient.deleteMessage(builder -> {
                    builder.queueUrl(queueUrl).receiptHandle(message.receiptHandle());
                });
            } catch (Exception e) {
                log.error("error processing message body {}", body, e);
            }
        }

        // send notifications transactional
        // List<String> sentMessages = new ArrayList<>();
        // for (int i = 0; i < notificationsToSend.size(); i++) {
        // Notification notification = notificationsToSend.get(i);
        // String receiptHandle = notificationReceipts.get(i);

        // try {
        // String messageId = sendNotificationAsEmail(notification);
        // log.info("successfully sent notification as email, message id = {}",
        // messageId);
        // sentMessages.add(messageId);
        // } catch (Exception e) {
        // log.error("could not send notification as email {}", notification, e);
        // continue;
        // }

        // // sqsClient.deleteMessage(builder -> {
        // // builder.queueUrl(notificationQueueUrl).receiptHandle(receiptHandle);
        // // });
        // }

        // return sentMessages;
    }

    ReceiveMessageResponse retrieveMessages(String url, int maxMessages) {
        ReceiveMessageResponse receiveMessageResponse = sqsClient.receiveMessage(
                request -> request.queueUrl(url).maxNumberOfMessages(maxMessages));

        return receiveMessageResponse;
    }

    // @SqsListener("${cloud.aws.sqs.queue}")
    // public void listenToSnsMessages(String message) {
    // log.info("Received message from SNS via SQS: " + message);
    // // Process the message here
    // }
}