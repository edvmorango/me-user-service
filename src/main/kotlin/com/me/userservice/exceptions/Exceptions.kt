package com.me.userservice.exceptions

import com.me.userservice.model.Address
import java.time.LocalDate

class UserNotFoundException: RuntimeException()

class CPFAlreadyExistsException(cpf: String): RuntimeException("The CPF [$cpf] already exists.")
class CPFInvalidException(cpf: String): RuntimeException("The CPF [$cpf] is invalid.")

class EmailsAlreadyExistsException(emails: List<String>): RuntimeException("Some of the emails [${emails.joinToString()}] already exists.")
class EmailsInvalidException(phones: List<String>):  RuntimeException("Some of the emails [${phones.joinToString()}] isn't valid.")

class EmptyFirstNameException: RuntimeException("The field [firstName] must be filled.")
class EmptyLastNameException: RuntimeException("The field [lastName] must be filled.")

class InvalidBirthDateException(birthDate: LocalDate): java.lang.RuntimeException("Invalid  birthDate [$birthDate].")

class EmptyAddressException: RuntimeException("The nested field [address] must be filled.")
class InvalidAddressNumberException(number: Int): RuntimeException("The number [$number] is invalid.")
class InvalidAddressZipCodeException(zipCode: String): RuntimeException("The zipCode [$zipCode] is invalid.")

class PhonesNumbersInvalidException(phones: List<String>):  RuntimeException("Some of the phones [${phones.joinToString()}] isn't valid.")

