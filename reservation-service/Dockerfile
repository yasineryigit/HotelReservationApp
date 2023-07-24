FROM openjdk:17
ADD /target/reservation-service-0.0.1-SNAPSHOT.jar reservation-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "reservation-service-0.0.1-SNAPSHOT.jar","--spring.pofiles.active=prod"]
