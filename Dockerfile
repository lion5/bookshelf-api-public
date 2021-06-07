FROM openjdk:11-jdk-slim AS builder

WORKDIR /app
COPY . .
RUN ./gradlew bootJar
RUN java -Djarmode=layertools -jar build/libs/bookshelf-*.jar extract

FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=builder app/spring-boot-loader/ ./
COPY --from=builder app/dependencies/ ./
COPY --from=builder app/snapshot-dependencies/ ./
COPY --from=builder app/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]