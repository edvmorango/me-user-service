package com.me.userservice.endpoint

import com.me.userservice.endpoint.response.BadRequestResponse
import com.me.userservice.endpoint.response.ConflictRequestResponse
import com.me.userservice.exceptions.ConflictException
import com.me.userservice.exceptions.FieldException
import com.me.userservice.exceptions.UserNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.server.RouterFunctionDsl
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Mono


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
                DELETE( "/{uuid}", userEndpoint::delete)
                PUT("/{uuid}", userEndpoint::update)
            }
        }
    }.filter { request, next ->
            next
                    .handle(request)
                    .onErrorResume {
                        when(it){
                            is UserNotFoundException  -> ServerResponse.notFound().build()
                            is FieldException -> ServerResponse.badRequest().body(Mono.just(BadRequestResponse(msg = it.message!!)), BadRequestResponse::class.java)
                            is ConflictException ->ServerResponse.status(HttpStatus.CONFLICT).body(Mono.just(ConflictRequestResponse(msg = it.message!!)), ConflictRequestResponse::class.java)
                            else -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
                        }
                    }
        }
}
