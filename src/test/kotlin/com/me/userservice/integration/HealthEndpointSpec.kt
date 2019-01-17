package com.me.userservice.integration

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("HealthEndpointSpec")
class HealthEndpointSpec: IntegrationBaseSpec() {

    @Test
    @DisplayName("Should health check")
    fun getBooks() {
        client.get().uri(contextPath + "/health")
                .exchange().expectStatus().isOk
    }

}
