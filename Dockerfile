FROM openjdk:15
VOLUME /tmp
ADD ./target/springboot-usuarios-0.0.1-SNAPSHOT.jar usuarios.jar
ENTRYPOINT ["java","-jar","/usuarios.jar"]