### First the database
Run `docker compose up -d` to run the database docker in detached mode

### Run the tests
Run `./gradlew test` to run the tests

### Build the JAR file
Run `./gradlew build` to run the JAR files

### Run the server
Run `java -jar ./build/libs/library-0.0.1-SNAPSHOT.jar` to run built jar file