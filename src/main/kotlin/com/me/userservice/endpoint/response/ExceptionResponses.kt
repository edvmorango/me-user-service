package com.me.userservice.endpoint.response


abstract class ExceptionResponse

data class BadRequestResponse(val status: Int = 400, val msg: String) : ExceptionResponse()
data class ConflictRequestResponse(val status: Int = 409, val msg: String) : ExceptionResponse()