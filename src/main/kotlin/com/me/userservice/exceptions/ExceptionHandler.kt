package com.me.userservice.exceptions

import com.fasterxml.jackson.databind.ObjectMapper
import com.me.userservice.endpoint.response.BadRequestResponse
import com.me.userservice.endpoint.response.ConflictRequestResponse
import com.me.userservice.endpoint.response.ExceptionResponse
import org.springframework.core.annotation.Order
import org.springframework.core.io.buffer.DataBufferFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebExceptionHandler
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono


@Component
@Order(-2)
class ExceptionHandler(val factory: DataBufferFactory, val mapper: ObjectMapper) : WebExceptionHandler {


    override fun handle(exchange: ServerWebExchange, ex: Throwable): Mono<Void> {

        fun parse(statusCode: HttpStatus, res: ExceptionResponse? = null): Mono<Void> {

            exchange.response.statusCode = statusCode

            return if(res != null){
                exchange.response.headers.contentType = MediaType.APPLICATION_JSON
                val body =  factory.wrap(mapper.writeValueAsBytes(res)).toMono()
                exchange.response.writeWith(body)
            } else {
                exchange.response.setComplete()
            }
        }

       return when(ex){
                        is UserNotFoundException  ->
                            parse(HttpStatus.NOT_FOUND)
                        is FieldException ->
                            parse(HttpStatus.BAD_REQUEST,  BadRequestResponse(msg = ex.message!!))
                        is ConflictException ->
                            parse(HttpStatus.CONFLICT,  ConflictRequestResponse(msg = ex.message!!))
                        else ->
                            parse(HttpStatus.INTERNAL_SERVER_ERROR)
                    }




        }

}