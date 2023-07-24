FROM openjdk:17
ADD /target/hotel-service-0.0.1-SNAPSHOT.jar hotel-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "hotel-service-0.0.1-SNAPSHOT.jar","--spring.pofiles.active=prod"]
