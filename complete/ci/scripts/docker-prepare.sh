#!/usr/bin/env bash

cd $(dirname $0)/../.. &&
java -version &&
./mvnw -s /settings.xml -DskipTests=true package &&
mkdir -p ../app/courier-service-day-publisher-app &&

cp target/version.txt ../app/ &&
cp -r app/src/main/resources/cql ../app/ &&
cp app/target/courier-service-day-publisher-app-exec.jar ../app/courier-service-day-publisher-app.jar &&
cp docker/entrypoint.sh ../app/ &&
cp docker/Dockerfile ../app/ &&
