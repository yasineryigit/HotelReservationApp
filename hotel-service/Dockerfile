# Build aşaması için Java 17 kullanın
FROM openjdk:17 AS build

WORKDIR /app

# Gerekli dosyaları kopyala
COPY pom.xml mvnw ./
RUN chmod +x mvnw
COPY .mvn .mvn
RUN ./mvnw dependency:resolve

# Kaynak kodunu kopyala ve uygulamayı paketle
COPY src src
RUN ./mvnw package -Dmaven.test.skip

# Çalıştırma aşaması için Alpine tabanlı Java 17 kullanın
FROM openjdk:17

WORKDIR /hotel-service

# Build aşamasında oluşturulan JAR dosyasını kopyala
COPY --from=build /app/target/*.jar /hotel-service/hotel-service.jar

ENTRYPOINT ["java","-jar","hotel-service.jar"]
