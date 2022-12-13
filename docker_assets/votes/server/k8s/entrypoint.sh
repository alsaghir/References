#!/bin/ash

echo $GRADLE_USER_HOME
ls -la /opt/gradle-home
gradle bootJar --info;
java -Djarmode=layertools -jar ./app/build/libs/*.jar extract --destination target/extracted;
