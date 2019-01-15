package com.me.userservice.endpoint

import com.me.userservice.endpoint.request.UserRequest
import com.me.userservice.endpoint.response.UserResponse
import com.me.userservice.endpoint.response.asResponse
import com.me.userservice.extensions.toNullable
import com.me.userservice.service.UserService
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToFlux
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class UserEndpoint(private val userService: UserService) {

    fun create(req : ServerRequest): Mono<ServerResponse> {

        val stream = req.bodyToFlux<UserRequest>()
                .map{ it.asDomain() }
                .flatMap(userService::create)
                .map { it.asResponse() }

        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(stream, UserResponse::class.java)
    }

    fun findByUuid(req: ServerRequest): Mono<ServerResponse> {

        val uuid = req.pathVariable("uuid")

        val resp = userService.findByUuid(uuid).map { it.asResponse() }

        return ServerResponse
                .ok()
                .body(resp, UserResponse::class.java)
    }

    fun list(req: ServerRequest): Mono<ServerResponse> {

        val stream = userService.list(
                cpf = req.queryParam("cpf").toNullable(),
                firstName = req.queryParam("firstName").toNullable(),
                lastName = req.queryParam("lastName").toNullable()


        ).map { it.asResponse() }

        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(stream, UserResponse::class.java)    }


}