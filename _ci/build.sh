#!/usr/bin/env bash

echo "=========================== Starting Build&Test Script ==========================="
PS4="\[\e[35m\]+ \[\e[m\]"
set -vex
pushd "$(dirname "${BASH_SOURCE[0]}")/../"

# If the branch is "master" and the commit is not a Pull Request then deploy the JAR SNAPSHOT artifacts
[ "${PULL_REQUEST}" = "false" ] && [ "${BRANCH_NAME}" = "master" ] && DEPLOY="deploy" || DEPLOY="install"

mvn -B -U clean source:jar source:test-jar ${DEPLOY} \
    -DaltDeploymentRepository=alfresco-internal::default::https://artifacts.alfresco.com/nexus/content/repositories/snapshots/

popd
set +vex
echo "=========================== Finishing Build&Test Script =========================="

