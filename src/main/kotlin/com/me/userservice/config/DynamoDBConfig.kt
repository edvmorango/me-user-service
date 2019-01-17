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
import software.amazon.awssdk.services.dynamodb.model.KeyType.*
import software.amazon.awssdk.services.dynamodb.model.ProjectionType.*
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

            val uuidKey = KeySchemaElement
                    .builder()
                    .keyType(HASH)
                    .attributeName("uuid")
                    .build()

            val atts = listOf<AttributeDefinition>(
                    AttributeDefinition
                        .builder()
                        .attributeName("uuid")
                        .attributeType(ScalarAttributeType.S)
                        .build(),
                    AttributeDefinition
                            .builder()
                            .attributeName("cpf")
                            .attributeType(ScalarAttributeType.S)
                            .build())

            val pt = ProvisionedThroughput
                    .builder()
                    .readCapacityUnits(10)
                    .writeCapacityUnits(10)
                    .build()

            val ks = KeySchemaElement
                    .builder()
                    .attributeName("cpf")
                    .keyType(HASH)
                    .build()

            val pj = Projection
                    .builder()
                    .projectionType(ALL)
                    .build()

            val si = GlobalSecondaryIndex
                    .builder()
                    .indexName("cpfIndex")
                    .provisionedThroughput(pt)
                    .projection(pj)
                    .keySchema(ks)
                    .build()

            val req = CreateTableRequest
                    .builder()
                    .tableName("user")
                    .keySchema(uuidKey)
                    .attributeDefinitions(atts)
                    .globalSecondaryIndexes(si)
                    .provisionedThroughput(pt)
                    .build()

            client.createTable(req)
        }
    }


    private fun deleteTable() {

        val credentials: AwsCredentials = AwsBasicCredentials.create(accessKey, secretKey)

        val client = DynamoDbClient.builder()
                .endpointOverride(URI.create(host!!))
                .region(Region.of(region!!))
                .credentialsProvider { credentials }
                .build()

        val req = DeleteTableRequest
                .builder()
                .tableName("user")
                .build()

        client.deleteTable(req)
    }


    fun resetTable() {
        deleteTable()
        createTable()
    }

}