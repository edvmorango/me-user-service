package com.me.userservice.service

import com.me.userservice.exceptions.*
import com.me.userservice.model.Address
import com.me.userservice.model.User
import com.me.userservice.repository.UserRepositoryInMemory
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import reactor.core.publisher.zip
import java.time.LocalDate

@DisplayName("UserServiceSpec")
class UserServiceSpec{

    val userRepository = UserRepositoryInMemory()
    val userService: UserService = UserServiceImpl(userRepository)

    val validAddress = Address("Rua Itapeva", 164, "69091201")
    val validUser = User(null,
            "Eduardo",
            "Vieira",
            "26164125197",
            LocalDate.of(1996,6,10),
            validAddress,
            listOf("jevmor@gmail.com", "jevmorr@gmail.com"),
            listOf("92991179136"),
            true)

    @Test
    @DisplayName("Shouldn't create a user without firstName")
    fun test1() {
        assertThrows<EmptyFirstNameException> { userService.create(validUser.copy(firstName = "")).block()  }

        assertThrows<EmptyFirstNameException> { userService.update("default",validUser.copy(firstName = "")).block()  }

    }


    @Test
    @DisplayName("Shouldn't create/update a user without lastName")
    fun test2() {
        assertThrows<EmptyLastNameException> { userService.create(validUser.copy(lastName = "")).block()  }

        assertThrows<EmptyLastNameException> { userService.update("default",validUser.copy(lastName = "")).block()  }

    }

    @Test
    @DisplayName("Shouldn't create/update a user without a valid CPF")
    fun test3() {
        assertThrows<CPFInvalidException> { userService.create(validUser.copy(cpf = "123")).block()  }
        assertThrows<CPFInvalidException> { userService.create(validUser.copy(cpf = "abac")).block()  }
        assertThrows<CPFInvalidException> { userService.create(validUser.copy(cpf = "")).block()  }
        assertThrows<CPFInvalidException> { userService.create(validUser.copy(cpf = "26164125199")).block()  }

        assertThrows<CPFInvalidException> { userService.update("default", validUser.copy(cpf = "123")).block()  }
        assertThrows<CPFInvalidException> { userService.update("default", validUser.copy(cpf = "abac")).block()  }
        assertThrows<CPFInvalidException> { userService.update("default", validUser.copy(cpf = "")).block()  }
        assertThrows<CPFInvalidException> { userService.update("default", validUser.copy(cpf = "26164125199")).block()  }

    }


    @Test
    @DisplayName("Shouldn't create/update a user without a valid birthDate ")
    fun test4() {
        assertThrows<InvalidBirthDateException> { userService.create(validUser.copy(birthDate = LocalDate.of(1900,6,10) )).block()  }
        assertThrows<InvalidBirthDateException> { userService.create(validUser.copy(birthDate = LocalDate.of(2100,6,10) )).block()  }
        assertThrows<InvalidBirthDateException> { userService.create(validUser.copy(birthDate = LocalDate.now())).block() }

        assertThrows<InvalidBirthDateException> { userService.update("default", validUser.copy(birthDate = LocalDate.of(1900,6,10) )).block()  }
        assertThrows<InvalidBirthDateException> { userService.update("default", validUser.copy(birthDate = LocalDate.of(2100,6,10) )).block()  }
        assertThrows<InvalidBirthDateException> { userService.update("default", validUser.copy(birthDate = LocalDate.now())).block() }


    }

    @Test
    @DisplayName("Shouldn't create/update a user without a address")
    fun test5() {
        val emptyAddress = validAddress.copy(address =  "")

        assertThrows<EmptyAddressException> { userService.create(validUser.copy(address = emptyAddress)).block()  }

        assertThrows<EmptyAddressException> { userService.update("default", validUser.copy(address = emptyAddress)).block()  }

    }


    @Test
    @DisplayName("Shouldn't create/update a user without a valid address number")
    fun test6() {
        val address = validAddress.copy(number = 0)
        val address2 = validAddress.copy(number = -10)

        assertThrows<InvalidAddressNumberException> { userService.create(validUser.copy(address = address)).block()  }
        assertThrows<InvalidAddressNumberException> { userService.create(validUser.copy(address = address2)).block()  }

        assertThrows<InvalidAddressNumberException> { userService.update("default",validUser.copy(address = address)).block()  }
        assertThrows<InvalidAddressNumberException> { userService.update("default",validUser.copy(address = address2)).block()  }

    }


    @Test
    @DisplayName("Shouldn't create/update a user without a valid address zipCode")
    fun test7() {
        val address = validAddress.copy(zipCode = "")
        val address2 = validAddress.copy(zipCode = "01231a")
        val address3 = validAddress.copy(zipCode = "-123123123")

        assertThrows<InvalidAddressZipCodeException> { userService.create(validUser.copy(address = address)).block()  }
        assertThrows<InvalidAddressZipCodeException> { userService.create(validUser.copy(address = address2)).block()  }
        assertThrows<InvalidAddressZipCodeException> { userService.create(validUser.copy(address = address3)).block()  }

        assertThrows<InvalidAddressZipCodeException> { userService.update("default", validUser.copy(address = address)).block()  }
        assertThrows<InvalidAddressZipCodeException> { userService.update("default", validUser.copy(address = address2)).block()  }
        assertThrows<InvalidAddressZipCodeException> { userService.update("default", validUser.copy(address = address3)).block()  }


    }


    @Test
    @DisplayName("Shouldn't create/update a user without a email")
    fun test8() {
        assertThrows<EmptyEmailsException> { userService.create(validUser.copy(emails = listOf())).block()  }

        assertThrows<EmptyEmailsException> { userService.update("default", validUser.copy(emails = listOf())).block()  }

    }


    @Test
    @DisplayName("Shouldn't create/update a user without a invalid email")
    fun test10() {
        assertThrows<EmailsInvalidException> { userService.create(validUser.copy(emails = listOf("somestring") )).block()  }

        assertThrows<EmailsInvalidException> { userService.update("default", validUser.copy(emails = listOf("somestring") )).block()  }

    }


    @Test
    @DisplayName("Shouldn't create/update a user without a  phone")
    fun test11() {
        assertThrows<EmptyPhonesException> { userService.create(validUser.copy(phones = listOf())).block()  }

        assertThrows<EmptyPhonesException> { userService.update("default", validUser.copy(phones = listOf())).block()  }

    }

    @Test
    @DisplayName("Shouldn't create/update a user with a invalid phone")
    fun test12() {
        assertThrows<PhonesNumbersInvalidException> { userService.create(validUser.copy(phones = listOf("aaaaa"))).block()  }

        assertThrows<PhonesNumbersInvalidException> { userService.update("default", validUser.copy(phones = listOf("aaaaa"))).block()  }

    }


}