# user-service

This project aims maintain users.

##TODOS

* Logging
* Better configuration
* Better deploy

### Prerequisites

Follow the instructions in the root project, before continue.

* JDK 8/11
* Gradle
* docker
* docker-compose

### Running

This projects support multiple environments the default will be always the top-level configurations at application.yml

Start containers:

At project path run:

```
docker-compose up -d
```

Run in default environment:

```
gradle bootRun
```

Run with a custom environment:

```
gradle bootRun -Dspring.profiles.active=*environment*
```

Environments:

* production

## Running the tests

```
gradle test
```

### Running

For informations copy *swagger.yml* to: ```https://editor.swagger.io/```

The base path is: *localhost:8080*

