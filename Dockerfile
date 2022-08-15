FROM openjdk:11

RUN mkdir /opt/results

RUN mkdir /app
WORKDIR /app
COPY target/minbenchmark-0.0.1-SNAPSHOT.jar /app/minbenchmark-3.jar
EXPOSE 9081

ENTRYPOINT ["java", "-jar", "/app/minbenchmark-3.jar"]