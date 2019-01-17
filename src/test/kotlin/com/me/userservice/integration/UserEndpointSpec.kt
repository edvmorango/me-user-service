package com.me.userservice.integration

import com.me.userservice.endpoint.request.AddressRequest
import com.me.userservice.endpoint.request.UserRequest
import com.me.userservice.endpoint.response.UserResponse
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.expectBodyList
import org.springframework.test.web.reactive.server.returnResult
import java.time.LocalDate


@DisplayName("UserEndpointSpec")
class UserEndpointSpec: IntegrationBaseSpec() {

    private val validRequestAddress = AddressRequest("Rua Itapeva", 164, "69091201")
    private val validRequestUser = UserRequest(
            "Eduardo",
            "Vieira",
            "26164125197",
            LocalDate.of(1996,6,10),
            validRequestAddress,
            listOf("jevmor@gmail.com", "jevmorr@gmail.com"),
            listOf("92991179136"))


    @Test
    @DisplayName("Should create and find user")
    fun test(){

        val created = client
                .post()
                .uri("$contextPath/user")
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(validRequestUser)
                .exchange()
                .expectStatus()
                .isCreated

        val returnResult = created.returnResult<UserResponse>().responseBody.blockFirst()!!

        client
                .get()
                .uri("$contextPath/user/${returnResult.uuid}")
                .exchange()
                .expectStatus()
                .isOk

    }

    @Test
    @DisplayName("Should create many users at same time and list")
    fun test2(){

        val validRequestUser1 =  validRequestUser.copy(cpf = "87179318949", emails = listOf("somemail1@mail.com"))

        val validRequestUser2 = validRequestUser.copy(cpf = "53131541253", emails = listOf("somemail2@mail.com"))
        val body = listOf(validRequestUser1, validRequestUser2)

        client
                .post()
                .uri("$contextPath/user")
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(body)
                .exchange()
                .expectStatus()
                .isCreated

        client
                .get()
                .uri("$contextPath/user/")
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<UserResponse>().hasSize(3)


    }

    @Test
    @DisplayName("Should create, update and find a updated user ")
    fun test3(){

        val validUserNew = validRequestUser.copy(cpf = "42260415342", emails = listOf("mmail@mail.com"))

        val created = client
                .post()
                .uri("$contextPath/user")
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(validUserNew)
                .exchange()
                .expectStatus()
                .isCreated

        val returnResult = created.returnResult<UserResponse>().responseBody.blockFirst()!!

        val updated =  client
                .put()
                .uri("$contextPath/user/${returnResult.uuid}")
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(validUserNew.copy(firstName = "new firstname"))
                .exchange()
                .expectStatus()
                .isOk

        val returnResultUpdated = updated.returnResult<UserResponse>().responseBody.blockFirst()!!


        val expectBody = client
                .get()
                .uri("$contextPath/user/${returnResult.uuid}")
                .exchange()
                .expectStatus()
                .isOk
                .expectBody(UserResponse::class.java).returnResult()

        assert(expectBody.responseBody!! == returnResultUpdated)

    }

    @Test
    @DisplayName("Should create many users at same time and list for firstName")
    fun test4(){

        val validRequestUser1 =  validRequestUser.copy(firstName = "John" ,cpf = "64284761188", emails = listOf("somemail1@4mail.com"))

        val validRequestUser2 = validRequestUser.copy(firstName = "John", cpf = "61183327897", emails = listOf("somemail2@4mail.com"))
        val body = listOf(validRequestUser1, validRequestUser2)

        client
                .post()
                .uri("$contextPath/user")
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(body)
                .exchange()
                .expectStatus()
                .isCreated

        client
                .get()
                .uri("$contextPath/user?firstName=${validRequestUser1.firstName}")
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<UserResponse>().hasSize(2)


    }


    @Test
    @DisplayName("Should create many users at same time and list for lastName")
    fun test5(){

        val validRequestUser1 =  validRequestUser.copy(lastName = "Doe" ,cpf = "16498430460", emails = listOf("doe@5mail.com"))

        val validRequestUser2 = validRequestUser.copy(lastName = "Doe", cpf = "31765057795", emails = listOf("doe2@5mail.com"))

        val validRequestUser3 = validRequestUser.copy(cpf = "85737421279", emails = listOf("somemail@5mail.com"))

        val body = listOf(validRequestUser1, validRequestUser2, validRequestUser3)

        client
                .post()
                .uri("$contextPath/user")
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(body)
                .exchange()
                .expectStatus()
                .isCreated

        client
                .get()
                .uri("$contextPath/user?lastName=${validRequestUser1.lastName}")
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<UserResponse>().hasSize(2)


    }


    @Test
    @DisplayName("Should create many users at same time and list for cpf")
    fun test6(){

        val validRequestUser1 =  validRequestUser.copy(cpf = "47536562179", emails = listOf("doe@6mail.com"))

        val validRequestUser2 = validRequestUser.copy(cpf = "67166992360", emails = listOf("doe2@6mail.com"))

        val body = listOf(validRequestUser1, validRequestUser2)

        client
                .post()
                .uri("$contextPath/user")
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(body)
                .exchange()
                .expectStatus()
                .isCreated

        client
                .get()
                .uri("$contextPath/user?cpf=${validRequestUser1.cpf}")
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<UserResponse>().hasSize(1)


    }

}