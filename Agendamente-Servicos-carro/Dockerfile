FROM ubuntu:latest
LABEL authors="marlus"

FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . .

RUN chmod +x mvnw

RUN ./mvnw clean package -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "target/Agendamente-Servicos-carro-0.0.1-SNAPSHOT.jar"]
