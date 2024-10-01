FROM amazoncorretto:21-alpine
VOLUME /tmp
ADD ./target/*.jar app.jar
ENV PORT=8082
EXPOSE $PORT
ENTRYPOINT ["java","-jar","/app.jar"]