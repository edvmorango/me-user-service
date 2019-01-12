package com.me.userservice.endpoint

import org.springframework.web.reactive.function.server.RouterFunctionDsl
import org.springframework.web.reactive.function.server.router


class Router(private val healthEndpoint: HealthEndpoint) {

    private val context = "/user-service/v1"
    private val health =  "/health"

    fun routes() = router {
        context.nest {
            health.nest {
                GET("", healthEndpoint::health)
            }
        }
    }

}