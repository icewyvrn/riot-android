#!/bin/bash

# Copyright 2018-2025 New Vector Ltd.
#
# SPDX-License-Identifier: AGPL-3.0-only
# Please see LICENSE in the repository root for full details.

# exit on any error
set -e

if [ "$#" -ne 1 ]; then
  echo "Usage: $0 GIT_BRANCH" >&2
  exit 1
fi

# which branch to build?
branch=$1

if [ -z "$branch" ]; then
   echo "Please specify the branch to build as a parameter"
   exit 1
fi

echo ${branch}

echo "Save current dir"
currentDir=`pwd`

echo "up dir"
cd ..

echo "remove sdk folder"
rm -rf matrix-android-sdk

echo "clone the matrix-android-sdk repository, and checkout ${branch} branch"
git clone -b ${branch} https://github.com/matrix-org/matrix-android-sdk

cd matrix-android-sdk

echo "Build matrix sdk from source"
./gradlew clean assembleRelease

cd ${currentDir}

echo "Copy freshly built matrix sdk to the libs folder"
# Ensure the lib is updated by removing the previous one
rm vector/libs/matrix-sdk.aar

cp ../matrix-android-sdk/matrix-sdk/build/outputs/aar/matrix-sdk-release-*.aar vector/libs/matrix-sdk.aar
