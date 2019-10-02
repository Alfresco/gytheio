#!/usr/bin/env bash

echo "=========================== Starting Build&Test Script ==========================="
PS4="\[\e[35m\]+ \[\e[m\]"
set -vex
pushd "$(dirname "${BASH_SOURCE[0]}")/../"

# Always build the ACS image, but only publish from the "master" branch
[ "${TRAVIS_PULL_REQUEST}" = "false" ] && [ "${TRAVIS_BRANCH}" = "master" ] && PROFILE="internal" || PROFILE="local"

# If the branch is "master" and the commit is not a Pull Request then deploy the JAR SNAPSHOT artifacts
[ "${TRAVIS_PULL_REQUEST}" = "false" ] && [ "${TRAVIS_BRANCH}" = "master" ] && DEPLOY="deploy" || DEPLOY="install"

mvn -B -U clean ${DEPLOY} "-P${PROFILE},docker-it-setup"

docker ps -a -q | xargs -r -l docker stop ; docker ps -a -q | xargs -r -l docker rm

popd
set +vex
echo "=========================== Finishing Build&Test Script =========================="

