package com.me.userservice.endpoint.request

import com.me.userservice.model.Address
import com.me.userservice.model.User
import java.time.LocalDate

data class UserRequest(
        val firstName: String,
        val lastName: String,
        val cpf: String,
        val birthDate: LocalDate,
        val address: AddressRequest,
        val emails: List<String>,
        val phones: List<String>) {

    fun asDomain() : User {
        return User(null, firstName, lastName, cpf, birthDate, address.asDomain(), emails, phones)
    }


}


data class AddressRequest(
        val address: String,
        val number: Int,
        val zipCode: String) {

    fun asDomain() : Address {
        return Address(address, number, zipCode)
    }

}

