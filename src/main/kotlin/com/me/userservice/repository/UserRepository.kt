package com.me.userservice.repository

import com.me.userservice.config.DynamoDBConfig
import com.me.userservice.model.User
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.*
import software.amazon.awssdk.services.dynamodb.model.ComparisonOperator.*
import java.util.*
import java.util.Collections.*
import kotlin.collections.HashMap


interface UserRepository {

    fun create(userItem: UserItem): Mono<UserItem>

    fun findByUuid(uuid: String): Mono<UserItem>

    fun findByCpf(cpf: String): Mono<UserItem>

    fun list(cpf: String? = null , firstName: String? = null, lastName: String? = null,  phones: List<String>? = null, emails: List<String>? = null ): Flux<UserItem>

    fun update(userItem: UserItem): Mono<UserItem>

    fun delete(uuid: String): Mono<Unit>

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
        val key = singletonMap("uuid", AttributeValue.builder().s(uuid).build())

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

    override fun findByCpf(cpf: String): Mono<UserItem> {
        val att = AttributeValue.builder().s(cpf).build()

        val condition = singletonMap("cpf",Condition
                .builder()
                .attributeValueList(att)
                .comparisonOperator(EQ)
                .build())

        val request = QueryRequest
                .builder()
                .tableName(tableName)
                .indexName("cpfIndex")
                .keyConditions(condition)
                .build()

        return Mono.fromFuture(client.query(request)).flatMap {
            if(it.count() > 0)
                toItem(it.items()[0]).toMono()
            else
                Mono.empty()
        }

    }

    override fun list(cpf: String?, firstName: String?, lastName: String?, phones: List<String>?, emails: List<String>? ): Flux<UserItem> {

        val filterAttributes = HashMap<String, AttributeValue?>()

        filterAttributes[":active"] = AttributeValue.builder().bool(true).build()


        if(cpf != null) {
            filterAttributes[":cpf"] = AttributeValue.builder().s(cpf).build()
        }

        if(firstName != null) {
            filterAttributes[":firstName"] = AttributeValue.builder().s(firstName).build()
        }

        if(lastName != null) {
            filterAttributes[":lastName"] = AttributeValue.builder().s(lastName).build()
        }

        if(emails != null) {
            emails.zip(0..emails.size).forEach{
                filterAttributes[":ss${it.second.toString().padStart(2, '0')}_emails"] = AttributeValue.builder().s(it.first).build()
            }
        }



        val expression = filterAttributes.keys.fold(""){ acc, s ->
            if(s.take(3) ==  ":ss")
                acc + " and contains(" +  s.drop(6) + ", $s)"
            else
                acc + " and " + s.drop(1) + " = " + s
        }.drop(5)




        val request = ScanRequest
                .builder()
                .tableName(tableName)
                .filterExpression(expression)
                .expressionAttributeValues(filterAttributes)
                .build()

        return Flux.from(client.scanPaginator(request).items()).map { toItem(it) }

    }

    override fun update(userItem: UserItem): Mono<UserItem> {
        val key = singletonMap("uuid", AttributeValue.builder().s(userItem.uuid).build())

        val attributes = userRequestBuilder(userItem)
                .filterNot { it.key == "uuid" }
                .mapValues { AttributeValueUpdate
                        .builder()
                        .value(it.value)
                        .build() }

        val request = UpdateItemRequest
                .builder()
                .tableName(tableName)
                .key(key)
                .attributeUpdates(attributes)
                .build()

        return Mono.fromFuture(client.updateItem(request)).map { userItem }
    }

    override fun delete(uuid: String): Mono<Unit> {

        val key = singletonMap("uuid", AttributeValue.builder().s(uuid).build())

        val userItem = HashMap<String, AttributeValue>()
        userItem["active"] = AttributeValue.builder().bool(false).build()

        val request = UpdateItemRequest
                .builder()
                .tableName(tableName)
                .key(key)
                .attributeUpdates(userItem.mapValues { AttributeValueUpdate.builder().value(it.value).build() })
                .build()

        return Mono.fromFuture(client.updateItem(request)).map { Unit }
    }
}