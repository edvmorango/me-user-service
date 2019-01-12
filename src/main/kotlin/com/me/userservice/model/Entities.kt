package com.me.userservice.model

import java.time.LocalDate


data class User(val uuid: String?,
                val firstName: String,
                val lastName: String,
                val cpf: String,
                val birthDate: LocalDate,
                val address: Address,
                val emails: List<String>,
                val phones: List<String>,
                val active: Boolean = true)

data class Address(val address: String, val number: Int, val zipCode: String)