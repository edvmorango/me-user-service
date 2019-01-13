package com.me.userservice.repository

import com.me.userservice.config.DynamoDBConfig
import com.me.userservice.model.User
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest
import software.amazon.awssdk.services.dynamodb.model.ScanRequest
import java.util.*


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
        userItem["active"] = AttributeValue.builder().bool(user.active).build()

        return userItem
    }

    private fun toItem(dic: Map<String, AttributeValue>): UserItem {

        val aDic = dic["address"]!!.m()

        val address = AddressItem(aDic["address"]!!.s(), aDic["number"]!!.n().toInt(), aDic["zipCode"]!!.s())


        return UserItem(
                dic["uuid"]!!.s(),
                dic["firstName"]!!.s(),
                dic["lastName"]!!.s(),
                dic["cpf"]!!.s(),
                dic["birthDate"]!!.s(),
                address,
                dic["phones"]!!.ss(),
                dic["emails"]!!.ss(),
                dic["active"]!!.bool())

    }



    override fun create(userItem: UserItem): Mono<UserItem> {

        val request = PutItemRequest
                .builder()
                .tableName(tableName)
                .item(userRequestBuilder(userItem))
                .build()

        return Mono.fromFuture(client.putItem(request)).map { userItem }

    }

    override fun findByUuid(uuid: String): Mono<UserItem> {
        val key = Collections.singletonMap("uuid", AttributeValue.builder().s(uuid).build())

        val request = GetItemRequest
                .builder()
                .tableName(tableName)
                .key(key)
                .build()

        return Mono.fromFuture(client.getItem(request)).flatMap {
            if(it.item().entries.size != 0)
                toItem(it.item()).toMono()
            else
                Mono.empty()
        }

    }

    override fun list(): Flux<UserItem> {

        val attributeValue = Collections.singletonMap(":active", AttributeValue.builder().bool(true).build())

        val request = ScanRequest
                .builder()
                .tableName(tableName)
                .filterExpression("active = :active")
                .expressionAttributeValues(attributeValue)
                .build()

        return Flux.from(client.scanPaginator(request).items()).map { toItem(it) }

    }



}