# ■■ STAGE 1 : build ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /src
# Copier pom.xml en premier pour profiter du cache Maven
COPY pom.xml .
RUN mvn dependency:go-offline -B
# Copier le code source et compiler (SANS les tests)
COPY src ./src
RUN mvn clean package -DskipTests=true -B
# Vérifier que le WAR a bien été créé
RUN ls -lh /src/target/*.war

# ■■ STAGE 2 : image de production ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
FROM tomcat:10.1-jre17-temurin-jammy

# Métadonnées de l'image
LABEL maintainer="DevOps Team"
LABEL description="Application Boutique - Jakarta EE 10 sur Tomcat 10.1"
LABEL version="1.0"

# Variables d'environnement Tomcat
ENV CATALINA_HOME=/usr/local/tomcat \
    JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC"

# Supprimer les webapps par défaut de Tomcat et installer curl pour HEALTHCHECK
RUN rm -rf $CATALINA_HOME/webapps/* && \
    apt-get update && \
    apt-get install -y --no-install-recommends curl && \
    rm -rf /var/lib/apt/lists/*

# Récupérer le WAR produit par le stage 1 et le déployer comme ROOT
COPY --from=build /src/target/app.war $CATALINA_HOME/webapps/ROOT.war

# Fixer les permissions pour que Tomcat puisse accéder au répertoire
RUN chmod -R 755 $CATALINA_HOME/webapps && \
    chmod -R 755 $CATALINA_HOME/work && \
    chmod -R 755 $CATALINA_HOME/temp && \
    chmod -R 755 $CATALINA_HOME/logs

EXPOSE 8080

# HEALTHCHECK pour vérifier que Tomcat est opérationnel
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD curl -f http://localhost:8080/ || exit 1

# Lancer Tomcat en avant-plan
CMD ["catalina.sh", "run"]