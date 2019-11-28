FROM openjdk:8
ADD target/fileprocessor-0.0.1-SNAPSHOT.jar fileprocessor-0.0.1-SNAPSHOT.jar
ENV JAVA_TOOL_OPTIONS -Dfile.encoding=UTF8
ENV LANG C.UTF-8
EXPOSE 9090
ENTRYPOINT ["java","-jar","fileprocessor-0.0.1-SNAPSHOT.jar"]