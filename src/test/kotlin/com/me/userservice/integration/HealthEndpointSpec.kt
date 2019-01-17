package com.me.userservice.integration

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("HealthEndpointSpec")
class HealthEndpointSpec: IntegrationBaseSpec() {

    @Test
    @DisplayName("Should health check")
    fun getBooks() {
        client.get().uri("/user-service/v1/health")
                .exchange().expectStatus().isOk
    }

}
