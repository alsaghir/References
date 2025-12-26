# Run using non-root user
# https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#container-images.dockerfiles
FROM eclipse-temurin:17-jdk-alpine
ARG USERNAME=developer
ARG GROUPNAME=developergroup
RUN addgroup "${GROUPNAME}"; adduser --ingroup "${GROUPNAME}" --disabled-password "${USERNAME}"
USER $USERNAME

VOLUME /tmp
ARG EXTRACTED=./target/extracted
COPY $EXTRACTED/dependencies/ ./
COPY $EXTRACTED/spring-boot-loader/ ./
COPY $EXTRACTED/snapshot-dependencies/ ./
COPY $EXTRACTED/application/ ./
ENTRYPOINT ["java","org.springframework.boot.loader.JarLauncher"]