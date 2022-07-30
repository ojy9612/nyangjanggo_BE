#!/bin/bash

CURRENT_PORT=$(cat /home/ec2-user/service_url.inc | grep -Po '[0-9]+' | tail -1)
TARGET_PORT=0

echo "> Current port of running WAS is ${CURRENT_PORT}."

if [ ${CURRENT_PORT} -eq 8081 ]; then
  TARGET_PORT=8082
elif [ ${CURRENT_PORT} -eq 8082 ]; then
  TARGET_PORT=8081
else
  echo "> No WAS is connected to nginx"
fi

TARGET_PID=$(lsof -Fp -i TCP:${TARGET_PORT} | grep -Po 'p[0-9]+' | grep -Po '[0-9]+')

if [ ! -z ${TARGET_PID} ]; then
  echo "> Kill WAS running at ${TARGET_PORT}."
  sudo kill -9 ${TARGET_PID}
fi

REPOSITORY=/home/ubuntu/hanghae99_team3
# shellcheck disable=SC2164
cd $REPOSITORY

APP_NAME=hanghae99_team3 #1
# shellcheck disable=SC2010
JAR_NAME=$(ls $REPOSITORY/build/libs/ | grep 'SNAPSHOT.jar' | tail -n 1)
JAR_PATH=$REPOSITORY/build/libs/$JAR_NAME

nohup java -jar -Dserver.port=${TARGET_PORT} ${JAR_PATH} > /home/ubuntu/hanghae99_team3/${TARGET_PORT}/nohup.out 2>&1 &
echo "> Now new WAS runs at ${TARGET_PORT}."
exit 0
