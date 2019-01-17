# user-service

This project aims maintain users.

### Prerequisites

Follow the instructions in the root project, before continue.

* JDK 8/11
* Gradle


### Running

This projects support multiple environments the default will be always the top-level configurations at application.yml

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


