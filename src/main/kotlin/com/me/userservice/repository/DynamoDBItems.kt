package com.me.userservice.repository

import com.me.userservice.endpoint.response.AddressResponse
import com.me.userservice.endpoint.response.UserResponse
import com.me.userservice.model.Address
import com.me.userservice.model.User
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class UserItem(val uuid: String,
                val firstName: String,
                val lastName: String,
                val cpf: String,
                val birthDate: String,
                val address: AddressItem,
                val emails: List<String>,
                val phones: List<String>,
                val active: Boolean = true) {

    fun asDomain(): User {
        return User(uuid, firstName, lastName, cpf, LocalDate.parse(birthDate, DateTimeFormatter.ISO_DATE), address.asDomain(), emails, phones)
    }

}

data class AddressItem(val address: String, val number: Int, val zipCode: String) {

    fun asDomain(): Address {
        return Address(address, number, zipCode)
    }

}


fun User.asItem(): UserItem {

    return UserItem(
            this.uuid!!,
            this.firstName,
            this.lastName,
            this.cpf,
            this.birthDate.toString(),
            this.address.asItem(),
            this.emails,
            this.phones)

}

fun Address.asItem(): AddressItem {
    return AddressItem(this.address, this.number, this.zipCode)
}