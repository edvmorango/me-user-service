package com.me.userservice.repository

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono

class UserRepositoryInMemory : UserRepository {

    val defaultUserItem =  UserItem(
            uuid =  "default",
            firstName =  "default user",
            lastName =  "default lastname",
            cpf =  "22530915296",
            birthDate =  "1996-10-10",
            address =  AddressItem("Some address", 199, "19125513"),
            emails =  listOf("default@mail.com"),
            phones =  listOf("00000000000"),
            active =  true)

    val duplicateUserItem =  UserItem(
            uuid =  "duplicate",
            firstName =  "default user",
            lastName =  "default lastname",
            cpf =  "37321555526",
            birthDate =  "1996-10-10",
            address =  AddressItem("Some address 2", 200, "12005110"),
            emails =  listOf("duplicate@mail.com"),
            phones =  listOf("11111111111"),
            active =  true)


    private var database : HashMap<String, UserItem> = hashMapOf(Pair("default", defaultUserItem), Pair("duplicate", duplicateUserItem))





    override fun create(userItem: UserItem): Mono<UserItem> {
        database[userItem.uuid] = userItem

        return userItem.toMono()
    }

    override fun findByUuid(uuid: String): Mono<UserItem> {

        return Mono.justOrEmpty(database[uuid])

    }

    override fun findByCpf(cpf: String): Mono<UserItem> {
        val item = database.values.filter { it.cpf == cpf}.firstOrNull()

        return Mono.justOrEmpty(item)

    }

    override fun list(cpf: String?, firstName: String?, lastName: String?, phones: List<String>?, emails: List<String>?): Flux<UserItem> {

        var values = database.values.filter { it.active }

        if(cpf != null)
            values = values.filter { it.cpf == cpf }

        if(firstName != null)
            values = values.filter { it.firstName == firstName }

        if(lastName != null)
            values = values.filter { it.lastName == lastName }

        if(emails != null)
            values = values.filterNot { it.emails.intersect(emails).isEmpty() }

        return Flux.fromArray(values.toTypedArray())
    }

    override fun update(userItem: UserItem): Mono<UserItem> {

        database[userItem.uuid] = userItem

        return userItem.toMono()

    }

    override fun delete(uuid: String): Mono<Unit> {

        val item = database.get(uuid)!!
        database[uuid] = item.copy(active = false)

        return Unit.toMono()
    }
}