FROM eclipse-temurin:17-jre-alpine
COPY "./target/uts-saberpro-platform-1.0.0.jar" "app.jar"
EXPOSE 8127
ENTRYPOINT ["java", "-jar", "app.jar"]