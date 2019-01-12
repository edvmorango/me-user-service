package com.me.userservice.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.AwsCredentials
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import java.net.URI


class DynamoDBConfig {

    @Value("\${aws.dynamodb.region}")
    val region: String? = null

    @Value("\${aws.dynamodb.host}")
    val host: String? = null

    @Value("\${aws.dynamodb.access-key}")
    val accessKey: String? = null

    @Value("\${aws.dynamodb.secret-key}")
    val secretKey: String? = null

    fun getDynamoClient(): DynamoDbAsyncClient {

        val credentials: AwsCredentials = AwsBasicCredentials.create(accessKey, secretKey)

        return DynamoDbAsyncClient
                .builder()
                .endpointOverride(URI.create(host!!))
                .region(Region.of(region!!))
                .credentialsProvider { credentials }
                .build()

    }

}