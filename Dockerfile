FROM adoptopenjdk/openjdk11-openj9:alpine
COPY build/libs/salechecker-1.0-SNAPSHOT-all.jar app.jar
ENTRYPOINT ["java", "-XX:+UseCGroupMemoryLimitForHeap", "-jar","app.jar"]