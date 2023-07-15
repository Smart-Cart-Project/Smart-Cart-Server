FROM eclipse-temurin:17-jdk-jammy
EXPOSE 8080
COPY /server/build/libs/*.jar ./smart-cart-server.jar
ENTRYPOINT ["java", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseContainerSupport", "-jar", "/smart-cart-server.jar"]