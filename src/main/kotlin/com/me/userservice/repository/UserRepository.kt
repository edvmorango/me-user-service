package com.me.userservice.repository

import com.me.userservice.model.User
import reactor.core.publisher.Mono

interface UserRepository {

    fun create(user: User): Mono<User>

    fun findByUuid(user: User): Mono<User>
    
}