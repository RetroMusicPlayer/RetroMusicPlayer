FROM eclipse-temurin:21-jdk

ENV ANDROID_SDK_ROOT=/opt/android-sdk
ENV ANDROID_HOME=$ANDROID_SDK_ROOT
ENV PATH=$PATH:$ANDROID_SDK_ROOT/cmdline-tools/latest/bin:$ANDROID_SDK_ROOT/platform-tools

RUN apt-get update && apt-get install -y \
    wget unzip git curl && \
    rm -rf /var/lib/apt/lists/*

RUN mkdir -p $ANDROID_SDK_ROOT/cmdline-tools && \
    wget -q https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip \
         -O /tmp/cmdline-tools.zip && \
    unzip -q /tmp/cmdline-tools.zip -d $ANDROID_SDK_ROOT/cmdline-tools && \
    mv $ANDROID_SDK_ROOT/cmdline-tools/cmdline-tools $ANDROID_SDK_ROOT/cmdline-tools/latest

# 2. Pre-install the platforms your build is asking for
RUN yes | sdkmanager --licenses && \
    sdkmanager "platform-tools" \
               "platforms;android-35" \
               "platforms;android-36" \
               "build-tools;35.0.0"

WORKDIR /app
COPY . .
