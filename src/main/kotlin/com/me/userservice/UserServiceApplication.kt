package com.me.userservice

import com.me.userservice.config.DynamoDBConfig
import com.me.userservice.config.context
import org.springframework.beans.factory.getBean
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext

@SpringBootApplication
class UserServiceApplication(val dynamoDBConfig: DynamoDBConfig) : ApplicationRunner {

	override fun run(args: ApplicationArguments?) {
		dynamoDBConfig.createTable()
	}
}

fun main(args: Array<String>) {
	runApplication<UserServiceApplication>(*args)
}


class ContextInitializer : ApplicationContextInitializer<GenericApplicationContext> {
	override fun initialize(ctx: GenericApplicationContext) {
		context.initialize(ctx)
	}
}