FROM openjdk:17
ADD /target/admin-server-0.0.1-SNAPSHOT.jar admin-server-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "admin-server-0.0.1-SNAPSHOT.jar","--spring.pofiles.active=prod"]
