package com.me.userservice.endpoint

import com.me.userservice.endpoint.response.HealthResponse
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono

class HealthEndpoint {

    fun health(req : ServerRequest): Mono<ServerResponse> {
        return ok().body(HealthResponse().toMono())
    }

}