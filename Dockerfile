FROM maven:3.9.6-eclipse-temurin-21 AS build

ENV DISPLAY=host.docker.internal:0.0

# Install only required libraries
RUN apt-get update && \
    apt-get install -y wget unzip libgtk-3-0 libgbm1 libx11-6 && \
    apt-get clean

# Download JavaFX SDK
RUN wget https://download2.gluonhq.com/openjfx/21/openjfx-21_linux-x64_bin-sdk.zip -O /tmp/openjfx.zip && \
    unzip /tmp/openjfx.zip -d /opt && \
    rm /tmp/openjfx.zip

# Download fonts (for Japanese characters) (Is this needed?)
RUN apt-get update && apt-get install -y \
    fonts-noto \
    fonts-noto-cjk \
    fonts-noto-core \
    fonts-noto-extra

WORKDIR /app

# Copy project
COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

CMD ["java", "--module-path", "/opt/javafx-sdk-21/lib", "--add-modules", "javafx.controls,javafx.fxml", "-jar", "target/trip_calculator.jar"]