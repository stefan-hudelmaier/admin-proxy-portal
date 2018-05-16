FROM java:8-jre-alpine

RUN mkdir -p /opt/application
ADD target/admin-proxy-portal-0.0.1-SNAPSHOT.jar /opt/application/application.jar

WORKDIR /opt/application
CMD java -jar application.jar
