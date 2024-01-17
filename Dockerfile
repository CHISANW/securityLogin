# Use the official OpenJDK base image with Alpine Linux
FROM adoptopenjdk:11-jre-hotspot

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file into the container at /app
COPY build/libs/security-0.0.1-SNAPSHOT.jar /app/login.jar

# Expose the port that the application will run on
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "login.jar"]