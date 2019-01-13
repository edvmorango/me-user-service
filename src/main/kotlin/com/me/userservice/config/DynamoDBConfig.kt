package com.me.userservice.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.AwsCredentials
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.*
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

    fun createTable() {

        val credentials: AwsCredentials = AwsBasicCredentials.create(accessKey, secretKey)

        val client = DynamoDbClient.builder()
                .endpointOverride(URI.create(host!!))
                .region(Region.of(region!!))
                .credentialsProvider { credentials }
                .build()

        if(!client.listTables().tableNames().contains("user")) {

            val key = KeySchemaElement
                    .builder()
                    .keyType(KeyType.HASH)
                    .attributeName("uuid")
                    .build()

            val att = AttributeDefinition
                    .builder()
                    .attributeName("uuid")
                    .attributeType(ScalarAttributeType.S)
                    .build()

            val pt = ProvisionedThroughput
                    .builder()
                    .readCapacityUnits(10)
                    .writeCapacityUnits(10)
                    .build()

            val req = CreateTableRequest
                    .builder()
                    .tableName("user")
                    .keySchema(key)
                    .attributeDefinitions(att)
                    .provisionedThroughput(pt)
                    .build()


            client.createTable(req)
        }
    }


}