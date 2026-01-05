#
# Gradle image for the build stage.
#
FROM gradle:9.2-jdk25-alpine AS build-image

#
# Set the working directory.
#
WORKDIR /app

#
# Copy the Gradle config, source code
# into the build container.
#
COPY build.gradle settings.gradle ./
COPY src ./src

#
# Build the application.
#
RUN ["gradle", "--no-daemon", "build", "-x", "test"]

# Java image for the application to run in.
#
FROM eclipse-temurin:25-alpine

#
# Create a non-root user and group for security purposes.
#
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

#
# Set the working directory.
#
WORKDIR /app

#
# Copy the jar file in and name it app.jar.
#
COPY --from=build-image /app/build/libs/AuthServer-0.0.1-SNAPSHOT.jar app.jar

#
# Change ownership of the files to the non-root user.
#
RUN chown -R appuser:appgroup /app

#
# Switch to the non-root user.
#
USER appuser

#
# The command to run when the container starts.
#
ENTRYPOINT ["java", "-jar", "app.jar"]