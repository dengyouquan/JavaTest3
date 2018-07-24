#!/bin/bash
set -e

sleep 15

echo 'start run app.jar'
java -jar app.jar

#tail -f /dev/null