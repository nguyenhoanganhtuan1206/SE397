FROM eclipse-temurin:17-jdk-focal as builder

COPY gradlew .
COPY gradle/ gradle/

COPY build.gradle .
COPY settings.gradle .

COPY src ./src

RUN chmod +x gradlew 

# Build the project
RUN ./gradlew clean build

FROM  eclipse-temurin:17-jdk-focal
WORKDIR /app

COPY --from=builder /build/libs/demo-0.0.1-SNAPSHOT.jar /app/app.jar
COPY . /app/

EXPOSE 80

CMD ["java", "-jar", "app.jar"]