FROM openjdk:17
ADD /target/accounting-service-0.0.1-SNAPSHOT.jar accounting-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "accounting-service-0.0.1-SNAPSHOT.jar","--spring.pofiles.active=prod"]
