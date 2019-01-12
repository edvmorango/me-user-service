package com.me.userservice.repository

import com.me.userservice.model.User
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface UserRepository {

    fun create(user: User): Mono<User>

    fun findByUuid(uuid: String): Mono<User>

    fun list(): Flux<User>
}