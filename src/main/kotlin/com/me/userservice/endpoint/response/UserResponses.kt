package com.me.userservice.endpoint.response

import com.me.userservice.model.Address
import java.time.LocalDate


data class UserResponse(
        val uuid: String?,
        val firstName: String,
        val lastName: String,
        val cpf: String,
        val birthDate: LocalDate,
        val address: AddressResponse,
        val emails: List<String>,
        val phones: List<String>
)


data class AddressResponse(
        val address: String,
        val number: Int,
        val zipCode: String
)
