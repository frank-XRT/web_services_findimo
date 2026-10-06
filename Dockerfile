# Etapa 1: Compilar el proyecto con Maven y Java 21
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
# Compila el proyecto omitiendo los tests
RUN mvn clean package -DskipTests

# Etapa 2: Crear la imagen ligera de Java 21 para ejecutar la app
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# Copia el .jar generado en la etapa anterior
COPY --from=build /app/target/*.jar app.jar
# Expone el puerto 8080 (asegúrate de que en tu application.properties tengas server.port=8080)
EXPOSE 8080
# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
