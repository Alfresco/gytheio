#!/usr/bin/env bash

echo "=========================== Starting WhiteSource Script ==========================="
PS4="\[\e[35m\]+ \[\e[m\]"
set -vex
pushd "$(dirname "${BASH_SOURCE[0]}")/../"


mvn -B clean install \
    -DskipTests org.whitesource:whitesource-maven-plugin:update \
    -Dorg.whitesource.failOnError=true \
    -Dorg.whitesource.forceUpdate=true \
    -Dorg.whitesource.checkPolicies=true \
    -Dorg.whitesource.forceCheckAllDependencies=true \
    -Dorg.whitesource.ignorePomModules=false \
    "-Dorg.whitesource.product=Media Management" \
    -Dmaven.wagon.http.pool=false


popd
set +vex
echo "=========================== Finishing WhiteSource Script =========================="

