FROM prototype-base-spark:1.0.0
WORKDIR /app
COPY target/scala-2.12/HelloWorld-assembly-0.1.0-SNAPSHOT.jar app.jar
CMD [“java”, “--jar”, “app.jar”]