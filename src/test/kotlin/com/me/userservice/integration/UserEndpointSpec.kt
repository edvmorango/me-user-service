package com.me.userservice.integration

import com.me.userservice.endpoint.request.AddressRequest
import com.me.userservice.endpoint.request.UserRequest
import com.me.userservice.model.Address
import com.me.userservice.model.User
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

        client.post().uri("$contextPath/user").contentType(MediaType.APPLICATION_JSON).syncBody(validRequestUser).exchange().expectStatus().isCreated

    }








}