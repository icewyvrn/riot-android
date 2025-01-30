/*
 * Copyright 2019-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package im.vector

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import im.vector.services.EventStreamServiceX
import org.matrix.androidsdk.core.Log


class VectorLifeCycleObserver : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onMoveToForeground() {
        Log.d(this::class.java.name, "Returning to foreground…")
        // https://issuetracker.google.com/issues/110237673
        // Work around for android 9 bug (service started in on resume can crash with IllegalState)
        // There is a workaround to avoid application crash.
        // Applications can get the process state in Activity.onResume() by calling
        // ActivityManager.getRunningAppProcesses() and avoid starting Service if the importance level
        // is lower than ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND.
        // If the device hasn’t fully awake, activities would be paused immediately and eventually be resumed again after its fully awake.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val activityManager = VectorApp.getInstance().getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
            if (activityManager != null) {
                val importance = activityManager.runningAppProcesses?.firstOrNull()?.importance
                // higher importance has lower number (?)
                if (importance != null && importance <= ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    EventStreamServiceX.onAppGoingToForeground(VectorApp.getInstance())
                }
            } else {
                EventStreamServiceX.onAppGoingToForeground(VectorApp.getInstance())
            }
        } else {
            EventStreamServiceX.onAppGoingToForeground(VectorApp.getInstance())
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onMoveToBackground() {
        Log.d(this::class.java.name, "Moving to background…")
        EventStreamServiceX.onAppGoingToBackground(VectorApp.getInstance())
    }

}