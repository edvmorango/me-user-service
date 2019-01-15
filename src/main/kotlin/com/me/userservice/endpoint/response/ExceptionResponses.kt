package com.me.userservice.endpoint.response


data class BadRequestResponse(val status: Int = 400, val msg: String)
data class ConflictRequestResponse(val status: Int = 409, val msg: String)