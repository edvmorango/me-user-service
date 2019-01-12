package com.me.userservice.endpoint

import com.me.userservice.service.UserService
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono

class UserEndpoint(private val userService: UserService) {

    fun create(req : ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok().build()
    }

    fun findByUuid(req: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok().build()
    }

    fun list(req: ServerRequest): Flux<ServerResponse> {
        return Flux.empty()
    }


}