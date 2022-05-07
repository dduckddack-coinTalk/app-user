#!/bin/bash
DEPLOY_DIR_PATH=/home/ubuntu/app
APP_PID=$(pgrep -f 'java -jar')

TARGET_JAR_PATH=$(ls -t $DEPLOY_DIR_PATH/*SNAPSHOT.jar | head -1)

JAR_NAME=${TARGET_JAR_PATH%.jar}
JAR_NAME=${JAR_NAME##*/}

if [ -n "$APP_PID"  ]; then
        kill -15 "$APP_PID"
        sleep 5
        echo "CLOSE $APP_PID "
fi

chmod +x "$TARGET_JAR_PATH"
nohup java -jar  "$TARGET_JAR_PATH" --spring.config.name=application >> "$DEPLOY_DIR_PATH"/log/"$JAR_NAME"_nohup.out 2>&1 &

