package com.markqtan.weather_api.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
public class AwsConfig {
    @Autowired
    private Environment env;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .overrideConfiguration(ClientOverrideConfiguration.builder().build())
                .credentialsProvider(getCredentialsProvider())
                .endpointOverride(getEndpoint())
                .region(getRegion())
                .forcePathStyle(true)
                .build();
    }

    @Bean
    public SnsClient snsClient() {
        return SnsClient.builder()
                .overrideConfiguration(ClientOverrideConfiguration.builder().build())
                .credentialsProvider(getCredentialsProvider())
                .endpointOverride(getEndpoint())
                .region(getRegion())
                // .forcePathStyle(true)
                
                // .endpointOverride(URI.create("http://localhost:4566")) // LocalStack endpoint
                // .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("test", "test"))) // Dummy
                // .region(Region.US_EAST_1) // Any valid region, as LocalStack is local
                .build();
    }

    @Bean
    // @Profile("local")
    public SqsClient localSqsClient() {
        return SqsClient.builder()
                .overrideConfiguration(ClientOverrideConfiguration.builder().build())
                .endpointOverride(getEndpoint())
                .credentialsProvider(getCredentialsProvider())
                // .credentialsProvider(AnonymousCredentialsProvider.create())
                .region(getRegion())
                .build();
    }

    private URI getEndpoint() {
        return URI.create(env.getProperty("aws.serviceEndpoint", ""));
    }   

    private Region getRegion() {
        return Region.of(env.getProperty("aws.signingRegion", ""));
    }

    private StaticCredentialsProvider getCredentialsProvider() {
        return StaticCredentialsProvider.create(AwsBasicCredentials.create(
                env.getProperty("aws.accessKey", ""),
                env.getProperty("aws.secretKey", "")));
    }
}
