package com.me.userservice.service

import com.me.userservice.exceptions.*
import com.me.userservice.model.User
import com.me.userservice.repository.UserRepository
import com.me.userservice.repository.asItem
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.switchIfEmpty
import reactor.core.publisher.toMono
import java.lang.RuntimeException
import java.time.Duration
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.*

interface UserService {

    fun create(user: User): Mono<User>

    fun findByUuid(uuid: String): Mono<User>

    fun list(cpf: String? = null , firstName: String? = null, lastName: String? = null,  phones: List<String>? = null, emails: List<String>? = null ): Flux<User>

}


class UserServiceImpl(private val userRepository: UserRepository): UserService {


    private fun validateUserFields(user: User): Mono<User> {

        val now = LocalDate.now()
        val between = ChronoUnit.YEARS.between(user.birthDate, now)
        return when {
            user.firstName.isEmpty() ->
                Mono.error(EmptyFirstNameException())
            user.lastName.isEmpty() ->
                Mono.error(EmptyLastNameException())
            !ValidationService.isValidCpf(user.cpf) ->
                Mono.error(CPFInvalidException(user.cpf))
            user.emails.map { ValidationService.isValidEmail(it) }.fold(true){ a, b -> a && b } ->
                Mono.error(EmailsInvalidException(user.emails))
            user.phones.map { ValidationService.isValidPhone(it) }.fold(true){ a, b -> a && b } ->
                Mono.error(PhonesNumbersInvalidException(user.emails))
            between >= 100 || between < 10 ->
                Mono.error(InvalidBirthDateException(user.birthDate))
            else ->
                Mono.just(user)
        }
    }

    private fun validateUser(user: User): Mono<User> {

            val cpfExists = userRepository
                    .findByCpf(user.cpf)
                    .flatMap{ Mono.error<User>(CPFAlreadyExistsException(user.cpf)) }

            val emailsExists = userRepository
                    .list(emails = user.emails)
                    .flatMap { Mono.error<User>(EmailsAlreadyExistsException(user.emails)) }

            return Flux
                    .merge(cpfExists, emailsExists)
                    .then(Mono.just(user))

    }


    override fun create(user: User): Mono<User> {

        return validateUserFields(user)
               .flatMap{validateUser(it)}
               .flatMap {
                   userRepository
                      .create(it.copy(uuid = UUID.randomUUID().toString()).asItem())
                      .map {u -> u.asDomain()}
               }
    }

    override fun findByUuid(uuid: String): Mono<User> {
        return userRepository.findByUuid(uuid).map { it.asDomain() }
    }

    override fun list(cpf: String?, firstName: String?, lastName: String?, phones: List<String>?, emails: List<String>?): Flux<User> {
        return userRepository.list(cpf, firstName, lastName, phones, emails).map { it.asDomain() }
    }
}