FROM gradle:jdk11-slim AS BUILD_IMAGE
COPY . /home/source/java
WORKDIR /home/source/java
USER root
RUN chown -R gradle /home/source/java
USER gradle
RUN gradle clean build


FROM openjdk:11-jre
VOLUME /application
WORKDIR /application
COPY --from=BUILD_IMAGE "/home/source/java/build/libs/user-service.jar" app.jar
CMD ["java","-Dspring.profiles.active=production","-jar","app.jar"]