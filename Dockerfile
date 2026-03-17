# ■■ STAGE 1 : build ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /src

# Copier pom.xml en premier pour profiter du cache Maven
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copier le code source et compiler (SANS les tests)
COPY src ./src
RUN mvn clean package -DskipTests=true -B

# ■■ STAGE 2 : image de production ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Récupérer uniquement le JAR produit par le stage 1
COPY --from=build /src/target/app.jar app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]