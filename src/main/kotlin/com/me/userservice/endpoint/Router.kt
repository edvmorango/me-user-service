package com.me.userservice.endpoint

import org.springframework.web.reactive.function.server.RouterFunctionDsl
import org.springframework.web.reactive.function.server.router


class Router(private val healthEndpoint: HealthEndpoint, private val userEndpoint: UserEndpoint) {

    private val context = "/user-service/v1"
    private val health =  "/health"
    private val user = "/user"

    fun routes() = router {
        context.nest {
            health.nest {
                GET("", healthEndpoint::health)
            }

            user.nest{
                POST("", userEndpoint::create)
                GET("", userEndpoint::list)
                GET("/{uuid}", userEndpoint::findByUuid)

            }
        }
    }

}