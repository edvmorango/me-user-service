package com.me.userservice.service

import com.me.userservice.model.User
import com.me.userservice.repository.UserRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface UserService {

    fun create(user: User): Mono<User>

    fun findyByUuid(uuid: String): Mono<User>

    fun list(): Flux<User>

}


class UserServiceImpl(userRepository: UserRepository): UserService {

    override fun create(user: User): Mono<User> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findyByUuid(uuid: String): Mono<User> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun list(): Flux<User> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}