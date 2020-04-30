#!/usr/bin/env bash

START_DOCKER=$(realpath $(dirname $0)/start_docker.sh)
STOP_DOCKER=$(realpath $(dirname $0)/stop_docker.sh)
ECR_LOGIN=$(realpath $(dirname $0)/ecr_login.sh)

${START_DOCKER}

${ECR_LOGIN}

cd $(dirname $0)/../.. &&
ls .mvn/wrapper/ &&
java -version &&
./mvnw -s /settings.xml verify site -Pintegration-test

TEST_EXIT_CODE=$?
cd app/target &&
aws s3 rm "s3://pulse-replacement-test-reports/Integration/courier-service-day-publisher/" --recursive
aws s3 cp courier-service-day-publisher-app-test-report.zip "s3://pulse-replacement-test-reports/Integration/courier-service-day-publisher/"

${STOP_DOCKER}

exit $TEST_EXIT_CODE