package com.me.userservice.integration

import com.me.userservice.UserServiceApplication
import com.me.userservice.UserServiceApplicationTests
import com.me.userservice.config.DynamoDBConfig
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.runApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@ExtendWith(SpringExtension::class)
class IntegrationBaseSpec{

    protected val client = WebTestClient.bindToServer()
            .baseUrl("http://127.0.0.1:8099")
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .build()

    protected val contextPath = "/user-service/v1"

//    @Autowired
//    private lateinit var dynamod: DynamoDBConfig
//
    @BeforeAll
    fun initApplication() {
        val runApplication = runApplication<UserServiceApplication>()
        val bean = runApplication.getBean(DynamoDBConfig::class.java)
        bean.resetTable()
    }
}


