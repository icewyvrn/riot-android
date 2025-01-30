#!/usr/bin/env bash

# Copyright 2018-2025 New Vector Ltd.
#
# SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
# Please see LICENSE files in the repository root for full details.

branch=${TRAVIS_BRANCH}

echo ${TRAVIS_BRANCH}

# If not on develop, exit, else we cannot get the list of modified files
# It is ok to check only when on develop branch
if [[ "${branch}" -eq 'develop' ]]; then
    echo "Check that the file 'CHANGES.rst' has been modified"
else
    echo "Not on develop branch"
    exit 0
fi

# git status

listOfModifiedFiles=`git diff --name-only HEAD ${branch}`

# echo "List of modified files by this PR:"
# echo ${listOfModifiedFiles}


if [[ ${listOfModifiedFiles} = *"CHANGES.rst"* ]]; then
  echo "CHANGES.rst has been modified!"
else
  echo "❌ Please add a line describing your change in CHANGES.rst"
  exit 1
fi
