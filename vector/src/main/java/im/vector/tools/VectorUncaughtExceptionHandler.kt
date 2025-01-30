/*
 * Copyright 2019-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package im.vector.tools

import android.content.Context
import android.os.Build
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import im.vector.BuildConfig
import im.vector.Matrix
import im.vector.VectorApp
import im.vector.util.BugReporter
import org.matrix.androidsdk.core.Log
import java.io.PrintWriter
import java.io.StringWriter

object VectorUncaughtExceptionHandler : Thread.UncaughtExceptionHandler {

    // key to save the crash status
    private const val PREFS_CRASH_KEY = "PREFS_CRASH_KEY"

    private var vectorVersion: String = ""
    private var matrixSdkVersion: String = ""

    private var previousHandler: Thread.UncaughtExceptionHandler? = null

    /**
     * Activate this handler
     */
    fun activate() {
        previousHandler = Thread.getDefaultUncaughtExceptionHandler()

        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    /**
     * An uncaught exception has been triggered
     *
     * @param thread    the thread
     * @param throwable the throwable
     * @return the exception description
     */
    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        val context = VectorApp.getInstance()

        if (context == null) {
            previousHandler?.uncaughtException(thread, throwable)
            return
        }

        PreferenceManager.getDefaultSharedPreferences(context).edit {
            putBoolean(PREFS_CRASH_KEY, true)
        }

        val b = StringBuilder()
        val appName = Matrix.getApplicationName()

        b.append(appName + " Build : " + BuildConfig.VERSION_CODE + "\n")
        b.append("$appName Version : $vectorVersion\n")
        b.append("SDK Version : $matrixSdkVersion\n")
        b.append("Phone : " + Build.MODEL.trim() + " (" + Build.VERSION.INCREMENTAL + " " + Build.VERSION.RELEASE + " " + Build.VERSION.CODENAME + ")\n")

        b.append("Memory statuses \n")

        var freeSize = 0L
        var totalSize = 0L
        var usedSize = -1L
        try {
            val info = Runtime.getRuntime()
            freeSize = info.freeMemory()
            totalSize = info.totalMemory()
            usedSize = totalSize - freeSize
        } catch (e: Exception) {
            e.printStackTrace()
        }

        b.append("usedSize   " + usedSize / 1048576L + " MB\n")
        b.append("freeSize   " + freeSize / 1048576L + " MB\n")
        b.append("totalSize   " + totalSize / 1048576L + " MB\n")

        b.append("Thread: ")
        b.append(thread.name)

        val a = VectorApp.getCurrentActivity()
        if (a != null) {
            b.append(", Activity:")
            b.append(a.localClassName)
        }

        b.append(", Exception: ")

        val sw = StringWriter()
        val pw = PrintWriter(sw, true)
        throwable.printStackTrace(pw)
        b.append(sw.buffer.toString())
        Log.e("FATAL EXCEPTION", b.toString())

        val bugDescription = b.toString()

        BugReporter.saveCrashReport(context, bugDescription)

        // Show the classical system popup
        previousHandler?.uncaughtException(thread, throwable)
    }

    fun setVersions(vectorVersion: String, matrixSdkVersion: String) {
        this.vectorVersion = vectorVersion
        this.matrixSdkVersion = matrixSdkVersion
    }

    /**
     * Tells if the application crashed
     *
     * @return true if the application crashed
     */
    fun didAppCrash(context: Context): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREFS_CRASH_KEY, false)
    }

    /**
     * Clear the crash status
     */
    fun clearAppCrashStatus(context: Context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit {
            remove(PREFS_CRASH_KEY)
        }
    }
}