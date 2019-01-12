package com.me.userservice.service

import com.me.userservice.model.User
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface UserService {

    fun create(user: User): Mono<User>

    fun findyByUuid(uuid: String): Mono<User>

    fun list(): Flux<User>

}
