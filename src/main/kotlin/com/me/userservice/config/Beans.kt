package com.me.userservice.config

import com.me.userservice.endpoint.HealthEndpoint
import com.me.userservice.endpoint.Router
import com.me.userservice.endpoint.UserEndpoint
import com.me.userservice.repository.UserRepository
import com.me.userservice.repository.UserRepositoryDynamoDB
import com.me.userservice.service.UserService
import com.me.userservice.service.UserServiceImpl


val context = org.springframework.context.support.beans {



    bean<DynamoDBConfig>()

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

}