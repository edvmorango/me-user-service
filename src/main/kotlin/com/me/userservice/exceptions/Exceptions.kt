package com.me.userservice.exceptions

import com.me.userservice.model.Address
import java.time.LocalDate

class UserNotFoundException: RuntimeException()

open class FieldException(msg: String) : RuntimeException(msg)
open class ConflictException(msg: String) : RuntimeException(msg)



class CPFAlreadyExistsException(cpf: String): ConflictException("The CPF [$cpf] already exists.")
class CPFInvalidException(cpf: String): FieldException("The CPF [$cpf] is invalid.")

class EmailsAlreadyExistsException(emails: List<String>): ConflictException("Some of the emails [${emails.joinToString()}] already exists.")
class EmailsInvalidException(phones: List<String>):  FieldException("Some of the emails [${phones.joinToString()}] isn't valid.")

class EmptyFirstNameException: FieldException("The field [firstName] must be filled.")
class EmptyLastNameException: FieldException("The field [lastName] must be filled.")

class InvalidBirthDateException(birthDate: LocalDate): FieldException("Invalid  birthDate [$birthDate].")

class EmptyAddressException: FieldException("The field [address] must be filled.")
class InvalidAddressNumberException(number: Int): FieldException("The number [$number] is invalid.")
class InvalidAddressZipCodeException(zipCode: String): FieldException("The zipCode [$zipCode] is invalid.")

class PhonesNumbersInvalidException(phones: List<String>):  FieldException("Some of the phones [${phones.joinToString()}] isn't valid.")

