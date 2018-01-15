FROM openjdk:8-jre-alpine
COPY target/RestServiceDemo-1.0.0-SNAPSHOT.jar /home/root/restservicedemo/
ENV myworkspace /home/root/restservicedemo
WORKDIR ${myworkspace}
EXPOSE 8080
USER 9000:9000
ENTRYPOINT ["java", "-jar","RestServiceDemo-1.0.0-SNAPSHOT.jar"]
