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



    private fun validateUserFields(user: User): Flux<Void> {

        val now = LocalDate.now()
        val between = ChronoUnit.YEARS.between(user.birthDate, now)
        return when {
            user.firstName.isEmpty() -> Flux.error(EmptyFirstNameException())
            user.lastName.isEmpty() -> Flux.error(EmptyLastNameException())
                    between >= 100 || between < 10
                    -> Flux.error(InvalidBirthDateException(user.birthDate))
            !ValidationService.isValidCpf(user.cpf) -> Flux.error(CPFInvalidException(user.cpf))
            // TODO email field validation
            // TODO phone field validation
            else -> {

                val cpfExists = userRepository.findByCpf(user.cpf).flatMap{Mono.error<Void>(CPFAlreadyExistsException(user.cpf))}

                Flux.merge(cpfExists)

            }
        }



    }

    override fun create(user: User): Mono<User> {

       val usr = user.copy(uuid = UUID.randomUUID().toString())

       return validateUserFields(usr)
               .then(userRepository
                        .create(usr.asItem())
                        .map {it.asDomain()})

    }

    override fun findByUuid(uuid: String): Mono<User> {
        return userRepository.findByUuid(uuid).map { it.asDomain() }
    }

    override fun list(cpf: String?, firstName: String?, lastName: String?, phones: List<String>?, emails: List<String>?): Flux<User> {
        return userRepository.list(cpf, firstName, lastName, phones, emails).map { it.asDomain() }
    }
}