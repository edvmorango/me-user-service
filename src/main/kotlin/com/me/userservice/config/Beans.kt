package com.me.userservice.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.me.userservice.endpoint.HealthEndpoint
import com.me.userservice.endpoint.Router
import com.me.userservice.endpoint.UserEndpoint
import com.me.userservice.repository.UserRepository
import com.me.userservice.repository.UserRepositoryDynamoDB
import com.me.userservice.service.UserService
import com.me.userservice.service.UserServiceImpl
import io.netty.buffer.ByteBufAllocator
import org.springframework.core.io.buffer.DataBufferFactory
import org.springframework.core.io.buffer.NettyDataBufferFactory


val context = org.springframework.context.support.beans {



    bean<DynamoDBConfig>()

    bean {
        ref<DynamoDBConfig>().getDynamoClient()
    }

    bean<UserRepository>{
        UserRepositoryDynamoDB(ref())
    }

    bean<UserService>{
        UserServiceImpl(ref())
    }

    bean<HealthEndpoint>()
    bean<UserEndpoint>()

    bean<Router>()
    bean {
        ref<Router>().routes()
    }

    bean{
        NettyDataBufferFactory(ByteBufAllocator.DEFAULT)
    }


}