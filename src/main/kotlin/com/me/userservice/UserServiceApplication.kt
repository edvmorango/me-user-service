package com.me.userservice

import com.me.userservice.config.context
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext

@SpringBootApplication
class UserServiceApplication

fun main(args: Array<String>) {
	runApplication<UserServiceApplication>(*args)
}


class ContextInitializer : ApplicationContextInitializer<GenericApplicationContext> {
	override fun initialize(ctx: GenericApplicationContext) {
		context.initialize(ctx)
	}
}