FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /app

COPY lib/orderLibrary-0.0.1-SNAPSHOT.jar /tmp/orderLibrary.jar
RUN mvn install:install-file \
    -Dfile=/tmp/orderLibrary.jar \
    -DgroupId=by.gsu.scherbak \
    -DartifactId=orderLibrary \
    -Dversion=0.0.1-SNAPSHOT \
    -Dpackaging=jar

COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY --from=build /app/target/orderService-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
