/*
 * Copyright 2019-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package im.vector.services

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import org.matrix.androidsdk.core.Log

/**
 * Parent class for all services
 */
abstract class VectorService : Service() {

    /**
     * Tells if the service self destroyed.
     */
    private var mIsSelfDestroyed = false

    override fun onCreate() {
        super.onCreate()

        Log.i(LOG_TAG, "## onCreate() : $this")
    }

    override fun onDestroy() {
        Log.i(LOG_TAG, "## onDestroy() : $this")

        if (!mIsSelfDestroyed) {
            Log.w(LOG_TAG, "## Destroy by the system : $this")
        }

        super.onDestroy()
    }

    protected fun myStopSelf() {
        Handler().postDelayed({
            mIsSelfDestroyed = true
            stopSelf()
        }, 100)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        private const val LOG_TAG = "VectorService"
    }
}