package com.me.userservice.repository

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono

class UserRepositoryInMemory : UserRepository {

    private var database =  HashMap<String, UserItem>()

    override fun create(userItem: UserItem): Mono<UserItem> {

        database[userItem.uuid] = userItem

        return userItem.toMono()
    }

    override fun findByUuid(uuid: String): Mono<UserItem> {

        return Mono.justOrEmpty(database[uuid])

    }

    override fun findByCpf(cpf: String): Mono<UserItem> {

        return Mono.justOrEmpty(database.filter { it.value.cpf == cpf}.values.firstOrNull())

    }

    override fun list(cpf: String?, firstName: String?, lastName: String?, phones: List<String>?, emails: List<String>?): Flux<UserItem> {

        var values = database.values.filter { it.active }

        if(cpf != null)
            values.filter { it.cpf == cpf }

        if(firstName != null)
            values.filter { it.firstName == firstName }

        if(lastName != null)
            values.filter { it.lastName == lastName }

        if(emails != null) {
            values.filter { u ->  emails.map { u.emails.contains(it)}.fold(true){a,b -> a && b}  }
        }

        return Flux.fromArray(values.toTypedArray())
    }

    override fun update(userItem: UserItem): Mono<UserItem> {

        database[userItem.uuid] = userItem

        return userItem.toMono()

    }

    override fun delete(uuid: String): Mono<Unit> {

        database.remove(uuid)

        return Unit.toMono()
    }
}