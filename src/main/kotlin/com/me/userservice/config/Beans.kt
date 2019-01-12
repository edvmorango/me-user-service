package com.me.userservice.config

import com.me.userservice.endpoint.HealthEndpoint
import com.me.userservice.endpoint.Router
import com.me.userservice.repository.UserRepository
import com.me.userservice.repository.UserRepositoryDynamoDB


val context = org.springframework.context.support.beans {



    bean<DynamoDBConfig>()

    bean<UserRepository>{
        UserRepositoryDynamoDB(ref())
    }

    bean<HealthEndpoint>()

    bean<Router>()
    bean {
        ref<Router>().routes()
    }

}