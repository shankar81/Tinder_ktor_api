FROM openjdk:11 as builder

COPY . .

RUN ./gradlew jar

FROM openjdk:11

COPY --from=builder /build/libs/tinderr-app.jar ./tinderr-app.jar

CMD ["java", "-jar", "tinderr-app.jar"]