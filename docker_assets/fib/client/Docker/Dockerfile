#Stage 1 - Install dependencies and build the app
FROM debian:latest AS build-env

ARG DEBIAN_FRONTEND=noninteractive

# For adding chrome repo
RUN apt-get update -qq && apt-get install -qq -y --no-install-recommends \
    apt-transport-https \
    gnupg2 \
    wget \
    ca-certificates

# Add repo for chrome stable
RUN wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add -
RUN echo 'deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main' | tee /etc/apt/sources.list.d/google-chrome.list

# For Dart SDK Installation
RUN echo 'deb [arch=amd64] https://storage.googleapis.com/download.dartlang.org/linux/debian stable main' | tee /etc/apt/sources.list.d/dart_stable.list

# Install flutter dependencies
RUN apt-get update -qq && apt-get install -qq -y --no-install-recommends \
    dart \
    google-chrome-stable \
    build-essential \
    gcc \
    locales \
    curl \
    git \
    unzip \
    libgconf-2-4 \
    gdb \
    libstdc++6 \
    libglu1-mesa \
    fonts-droid-fallback \
    lib32stdc++6 \
    python3 \
    clang \
    cmake \
    ninja-build \
    pkg-config \
    libgtk-3-dev \
    liblzma-dev  && \
    apt-get clean

# Clone the flutter repo
RUN git clone https://github.com/flutter/flutter.git /usr/local/flutter

# Set flutter path
# RUN /usr/local/flutter/bin/flutter doctor -v
ENV PATH="/usr/local/flutter/bin:/usr/local/flutter/bin/cache/dart-sdk/bin:${PATH}"
RUN flutter config --no-analytics --enable-web --no-enable-android --no-enable-ios

# Run flutter doctor
RUN flutter doctor -v
# Enable flutter web
RUN flutter channel master
RUN flutter upgrade
RUN flutter pub global activate webdev

# Copy files to container and build
WORKDIR /app/
COPY pubspec.yaml .
RUN flutter pub get
COPY . .
# RUN pwd
# RUN ls -la ./build/web
# RUN ls -la /app
# RUN ls -la /app/build/web/canvaskit
# ARG FLUTTER_WEB_CANVASKIT_URL=/app/build/web/canvaskit
RUN flutter build web
# RUN flutter build web
# RUN pwd

# Stage 2 - Create the run-time image
FROM nginx:1.23.1-alpine
COPY ./Docker/nginx_default.conf /etc/nginx/conf.d/default.conf
COPY --from=build-env /app/build/web /usr/share/nginx/html
