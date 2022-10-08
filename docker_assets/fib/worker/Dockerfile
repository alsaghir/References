# Build stage
FROM maven:3-eclipse-temurin-17-alpine as build

WORKDIR /workspace/app

COPY pom.xml .

RUN mvn -B dependency:resolve dependency:resolve-plugins
COPY src src

RUN mvn -B -o install && \
    mkdir -p target/extracted && \
    java -Djarmode=layertools -jar target/*.jar extract --destination target/extracted

# Run stage using non-root user
# https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#container-images.dockerfiles
FROM eclipse-temurin:17-jdk-alpine
ARG USERNAME=developer
ARG GROUPNAME=developergroup
RUN addgroup "${GROUPNAME}"; adduser --ingroup "${GROUPNAME}" --disabled-password "${USERNAME}"
USER $USERNAME

VOLUME /tmp
ARG EXTRACTED=/workspace/app/target/extracted
COPY --from=build ${EXTRACTED}/dependencies/ ./
COPY --from=build ${EXTRACTED}/spring-boot-loader/ ./
COPY --from=build ${EXTRACTED}/snapshot-dependencies/ ./
COPY --from=build ${EXTRACTED}/application/ ./
ENTRYPOINT ["java","org.springframework.boot.loader.JarLauncher"]