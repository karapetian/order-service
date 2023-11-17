FROM --platform=linux/amd64 public.ecr.aws/docker/library/eclipse-temurin:17-jdk-alpine as builder
WORKDIR application
ARG JAR_FILE=target/order-service-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM --platform=linux/amd64 public.ecr.aws/docker/library/ubuntu:latest
WORKDIR /app
RUN apt-get update && apt-get install -y openjdk-17-jdk
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader ./
COPY --from=builder application/application/ ./
CMD ["java", "org.springframework.boot.loader.JarLauncher"]