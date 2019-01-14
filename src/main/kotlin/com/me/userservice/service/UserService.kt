package com.me.userservice.service

import com.me.userservice.exceptions.CPFAlreadyExistsException
import com.me.userservice.model.User
import com.me.userservice.repository.UserRepository
import com.me.userservice.repository.asItem
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.switchIfEmpty
import reactor.core.publisher.toMono
import java.lang.RuntimeException
import java.time.Duration
import java.util.*

interface UserService {

    fun create(user: User): Mono<User>

    fun findByUuid(uuid: String): Mono<User>

    fun list(): Flux<User>

}


class UserServiceImpl(private val userRepository: UserRepository): UserService {


    private fun validateUser(user: User): Flux<Void> {

        val cpfExists = userRepository.findByCpf(user.cpf).flatMap{Mono.error<Void>(CPFAlreadyExistsException(user.cpf))}

        return Flux.merge(cpfExists, cpfExists)
    }

    override fun create(user: User): Mono<User> {

       val usr = user.copy(uuid = UUID.randomUUID().toString())

       return validateUser(usr)
               .then(userRepository
                        .create(usr.asItem())
                        .map {it.asDomain()})

    }

    override fun findByUuid(uuid: String): Mono<User> {
        return userRepository.findByUuid(uuid).map { it.asDomain() }
    }

    override fun list(): Flux<User> {
        return userRepository.list().map { it.asDomain() }
    }
}