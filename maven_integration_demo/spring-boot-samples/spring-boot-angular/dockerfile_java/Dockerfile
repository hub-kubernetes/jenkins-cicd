FROM openjdk:8-jre
RUN mkdir /home/app
WORKDIR /home/app
COPY ../target/spring-boot-angular-1.4.0.BUILD-SNAPSHOT.jar . 
CMD ["java","-jar","spring-boot-angular-1.4.0.BUILD-SNAPSHOT.jar"]
