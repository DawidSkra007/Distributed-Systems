FROM openjdk:11.0.8-jre
ENV HOST=localhost
COPY target/*-jar-with-dependencies.jar /producer.jar
CMD sleep 10 && java -jar /producer.jar $HOST