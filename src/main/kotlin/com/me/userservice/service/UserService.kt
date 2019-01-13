package com.me.userservice.service

import com.me.userservice.model.User
import com.me.userservice.repository.UserRepository
import com.me.userservice.repository.asItem
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration
import java.util.*

interface UserService {

    fun create(user: User): Mono<User>

    fun findByUuid(uuid: String): Mono<User>

    fun list(): Flux<User>

}


class UserServiceImpl(private val userRepository: UserRepository): UserService {

    override fun create(user: User): Mono<User> {

       val user = user.copy(uuid = UUID.randomUUID().toString())

        return userRepository.create(user.asItem()).map {it.asDomain()}

    }

    override fun findByUuid(uuid: String): Mono<User> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun list(): Flux<User> {
        return userRepository.list().map { it.asDomain() }
    }
}