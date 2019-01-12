package com.me.userservice.endpoint.request

import java.time.LocalDate

data class UserRequest(
        val firstName: String,
        val lastName: String,
        val cpf: String,
        val birthDate: LocalDate,
        val address: AddressRequest,
        val emails: List<String>,
        val phones: List<String>)


data class AddressRequest(
        val address: String,
        val number: Int,
        val zipCode: String
)
