/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package im.vector.util

import android.content.Context
import android.util.Log
import java.io.File

private const val LOG_TAG = "FileUtils"

// Implementation should return true in case of success
typealias ActionOnFile = (file: File) -> Boolean

/* ==========================================================================================
 * Delete
 * ========================================================================================== */

fun deleteAllFiles(context: Context) {
    Log.v(LOG_TAG, "Delete cache dir:")
    recursiveActionOnFile(context.cacheDir, ::deleteAction)

    Log.v(LOG_TAG, "Delete files dir:")
    recursiveActionOnFile(context.filesDir, ::deleteAction)
}

private fun deleteAction(file: File): Boolean {
    if (file.exists()) {
        Log.v(LOG_TAG, "deleteFile: $file")
        return file.delete()
    }

    return true
}

/* ==========================================================================================
 * Log
 * ========================================================================================== */

fun lsFiles(context: Context) {
    Log.v(LOG_TAG, "Content of cache dir:")
    recursiveActionOnFile(context.cacheDir, ::logAction)

    Log.v(LOG_TAG, "Content of files dir:")
    recursiveActionOnFile(context.filesDir, ::logAction)
}

private fun logAction(file: File): Boolean {
    if (file.isDirectory) {
        Log.d(LOG_TAG, file.toString())
    } else {
        Log.d(LOG_TAG, file.toString() + " " + file.length() + " bytes")
    }
    return true
}

/* ==========================================================================================
 * Private
 * ========================================================================================== */

/**
 * Return true in case of success
 */
private fun recursiveActionOnFile(file: File, action: ActionOnFile): Boolean {
    if (file.isDirectory) {
        file.list().forEach {
            val result = recursiveActionOnFile(File(file, it), action)

            if (!result) {
                // Break the loop
                return false
            }
        }
    }

    return action.invoke(file)
}

