FROM openjdk:17
COPY "./target/puts-saberpro-platform-1.0.0.jar" "app.jar"
EXPOSE "8127"
ENTRYPOINT [ "java", "-jar", "app.jar" ]