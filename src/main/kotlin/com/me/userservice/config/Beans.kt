package com.me.userservice.config

import com.me.userservice.endpoint.HealthEndpoint
import com.me.userservice.endpoint.Router


val context = org.springframework.context.support.beans {

    bean<HealthEndpoint>()

    bean<Router>()
    bean {
        ref<Router>().routes()
    }

}