FROM openjdk:25-slim

LABEL author=stanleyvladimir2005@gmail.com

COPY "./build/libs/mediapp-backend-springboot3-0.0.1-SNAPSHOT.jar" "app.jar"

ENTRYPOINT ["java", "-jar", "/app.jar"]