FROM openjdk:17
ADD /target/notification-service-0.0.1-SNAPSHOT.jar notification-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "notification-service-0.0.1-SNAPSHOT.jar","--spring.pofiles.active=prod"]
