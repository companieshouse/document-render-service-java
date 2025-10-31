#!/bin/bash

PORT=8080

exec java -jar -Dserver.port="${PORT}" "document-render-service-java.jar"