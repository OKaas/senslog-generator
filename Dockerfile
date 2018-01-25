FROM openjdk:8-jre-alpine

COPY *.jar /senslog-generator.jar

# Run java
CMD ["java", "-jar", "/senslog-generator.jar"]