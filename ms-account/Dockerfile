FROM amazoncorretto:21-alpine
VOLUME /tmp
ADD ./target/*.jar app.jar
ENV PORT=8080
EXPOSE $PORT
ENTRYPOINT ["java","-jar","/app.jar"]