package com.me.userservice.repository

import com.me.userservice.config.DynamoDBConfig
import com.me.userservice.model.User
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface UserRepository {

    fun create(user: User): Mono<User>

    fun findByUuid(uuid: String): Mono<User>

    fun list(): Flux<User>
}

class UserRepositoryDynamoDB(val dynamoDBConfig: DynamoDBConfig) : UserRepository {


    override fun create(user: User): Mono<User> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findByUuid(uuid: String): Mono<User> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun list(): Flux<User> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}