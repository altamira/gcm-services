FROM java:8

ADD target/gcm-notification-service-0.0.4-SNAPSHOT.jar /opt/gcm-notification-service/

EXPOSE 8080

WORKDIR /opt/gcm-notification-service/

CMD ["java", "-Xms128m", "-Xmx1g", "-jar", "gcm-notification-service-0.0.4-SNAPSHOT.jar"]