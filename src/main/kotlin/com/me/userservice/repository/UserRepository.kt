package com.me.userservice.repository

import com.me.userservice.config.DynamoDBConfig
import com.me.userservice.model.User
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest
import software.amazon.awssdk.services.dynamodb.DynamoDbClient



interface UserRepository {

    fun create(userItem: UserItem): Mono<UserItem>

    fun findByUuid(uuid: String): Mono<UserItem>

    fun list(): Flux<UserItem>
}

class UserRepositoryDynamoDB(private val client: DynamoDbAsyncClient) : UserRepository {

    private val tableName = "user"

    private fun userRequestBuilder(user: UserItem): HashMap<String, AttributeValue> {

        val userItem = HashMap<String, AttributeValue>()


        val addressItem =  HashMap<String, AttributeValue>()

        addressItem["address"] = AttributeValue.builder().s(user.address.address).build()
        addressItem["number"] = AttributeValue.builder().n(user.address.number.toString()).build()
        addressItem["zipCode"] = AttributeValue.builder().s(user.address.zipCode).build()



        userItem["uuid"] = AttributeValue.builder().s(user.uuid).build()
        userItem["firstName"] = AttributeValue.builder().s(user.firstName).build()
        userItem["lastName"] = AttributeValue.builder().s(user.lastName).build()
        userItem["cpf"] = AttributeValue.builder().s(user.cpf).build()
        userItem["birthDate"] = AttributeValue.builder().s(user.birthDate).build()
        userItem["address"] = AttributeValue.builder().m(addressItem).build()
        userItem["phones"] = AttributeValue.builder().ss(user.phones).build()
        userItem["emails"] = AttributeValue.builder().ss(user.emails).build()


        return userItem
    }

    override fun create(userItem: UserItem): Mono<UserItem> {

        val request = PutItemRequest.builder()
                .tableName(tableName)
                .item(userRequestBuilder(userItem))
                .build()

        return Mono.fromFuture(client.putItem(request)).map { userItem }

    }

    override fun findByUuid(uuid: String): Mono<UserItem> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun list(): Flux<UserItem> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}