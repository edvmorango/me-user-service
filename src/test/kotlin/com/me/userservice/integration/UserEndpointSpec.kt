package com.me.userservice.integration

import com.me.userservice.endpoint.request.AddressRequest
import com.me.userservice.endpoint.request.UserRequest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
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
    @DisplayName("Should create a user")
    fun test(){

        client
                .post()
                .uri("$contextPath/user")
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(validRequestUser)
                .exchange()
                .expectStatus()
                .isCreated

    }

    @Test
    @DisplayName("Should create many users at same time")
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

    }







}