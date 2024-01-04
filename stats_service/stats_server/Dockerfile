FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar stats_service.jar
ENTRYPOINT ["java","-jar","/stats_service.jar"]