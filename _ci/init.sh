#!/usr/bin/env bash

echo "=========================== Starting Init Script ==========================="
PS4="\[\e[35m\]+ \[\e[m\]"
set -vex
pushd "$(dirname "${BASH_SOURCE[0]}")/../"

mkdir -p ${HOME}/.m2 && cp -rf _ci/settings.xml ${HOME}/.m2/
find "${HOME}/.m2/repository/" -type d -name "*-SNAPSHOT*" | xargs -r -l rm -rf

sudo apt-get update
sudo apt-get install -q -y  imagemagick ghostscript ffmpeg

# ImageMagick6's default security policy doesn't allow it to parse PDFs
sudo sed -i '/PDF/s/none/read|write/' /etc/ImageMagick-6/policy.xml

popd
set +vex
echo "=========================== Finishing Init Script =========================="

