FROM maven:3-eclipse-temurin-17-alpine AS builder

ENV APP_HOME /apps

RUN ["mkdir", "-p", "$APP_HOME"]
WORKDIR $APP_HOME

COPY pom.xml ./pom.xml
RUN mvn dependency:go-offline
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17.0.6_10-jre-alpine

ENV APP_HOME /apps

RUN ["mkdir", "-p", "$APP_HOME"]
WORKDIR $APP_HOME

COPY --from=builder $APP_HOME/target/eureka-server-1.0-SNAPSHOT.jar ./eureka-server-1.0-SNAPSHOT.jar

ENV ACTIVE_PROFILE dev

ENTRYPOINT ["java", "-Dspring.profiles.active=${ACTIVE_PROFILE}", "-jar", "eureka-server-1.0-SNAPSHOT.jar"]
EXPOSE 8761