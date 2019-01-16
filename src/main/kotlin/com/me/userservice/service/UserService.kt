package com.me.userservice.service

import com.me.userservice.exceptions.*
import com.me.userservice.model.User
import com.me.userservice.repository.UserItem
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

    fun update(uuid: String, user: User): Mono<User>

    fun delete(uuid: String): Mono<Unit>

}


class UserServiceImpl(private val userRepository: UserRepository): UserService {


    private fun validateUserFields(user: User): Mono<User> {

        val now = LocalDate.now()
        val between = ChronoUnit.YEARS.between(user.birthDate, now)

        val address = user.address

        return when {
            user.firstName.isEmpty() ->
                Mono.error(EmptyFirstNameException())
            user.lastName.isEmpty() ->
                Mono.error(EmptyLastNameException())
            !ValidationService.isValidCpf(user.cpf) ->
                Mono.error(CPFInvalidException(user.cpf))
            user.emails.isEmpty() ->
                Mono.error(EmptyEmailsException())
            user.emails.map { !ValidationService.isValidEmail(it) }.fold(false){ a, b -> a || b } ->
                Mono.error(EmailsInvalidException(user.emails))
            user.phones.isEmpty() ->
                Mono.error(EmptyPhonesException())
            user.phones.map { !ValidationService.isValidPhone(it) }.fold(false){ a, b -> a || b } ->
                Mono.error(PhonesNumbersInvalidException(user.emails))
            between >= 100 || between < 10 ->
                Mono.error(InvalidBirthDateException(user.birthDate))
            address.address.isEmpty() ->
                Mono.error(EmptyAddressException())
            address.number <= 0 ->
                Mono.error(InvalidAddressNumberException(address.number))
            !address.zipCode.map {it.isDigit()}.fold(true){a , b -> a && b}  || address.zipCode.isEmpty() ->
                Mono.error(InvalidAddressZipCodeException(address.zipCode))
            address.zipCode.toLong() <= 0 ->
                Mono.error(InvalidAddressZipCodeException(address.zipCode))




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

    private fun validateUserUpdate(new: User, current: User): Mono<User> {

        val cpfExists =
                if(new.cpf == current.cpf)
                    new.toMono()
                else
                     userRepository
                             .findByCpf(current.cpf)
                             .flatMap{ Mono.error<User>(CPFAlreadyExistsException(current.cpf)) }

        val filteredEmails = new.emails.filterNot { current.emails.contains(it)}

        val emailExists =
                if(filteredEmails.isEmpty())
                    Flux.just(current)
                else
                    userRepository
                            .list(emails = filteredEmails)
                            .flatMap { Mono.error<User>(EmailsAlreadyExistsException(current.emails)) }

        return Flux
                .merge(cpfExists, emailExists)
                .then(Mono.just(current))


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
        return userRepository
                .findByUuid(uuid)
                .map { it.asDomain() }
                .switchIfEmpty(Mono.error(UserNotFoundException()))
    }

    override fun list(cpf: String?, firstName: String?, lastName: String?, phones: List<String>?, emails: List<String>?): Flux<User> {
        return userRepository
                .list(cpf, firstName, lastName, phones, emails)
                .map { it.asDomain() }
    }

    override fun update(uuid: String, user: User): Mono<User> {
        return userRepository
                .findByUuid(uuid)
                .switchIfEmpty(Mono.error(UserNotFoundException()))
                .flatMap { currItem ->  validateUserFields(user).flatMap { validateUserUpdate(user, currItem.asDomain()) } }
                .flatMap { userRepository
                            .update(user.copy(uuid = uuid).asItem())
                            .map { u -> u.asDomain() }
                }
    }

    override fun delete(uuid: String): Mono<Unit> {
        return userRepository
                .findByUuid(uuid)
                .flatMap { userRepository.delete(uuid) }
                .switchIfEmpty(Mono.error(UserNotFoundException()))

    }
}