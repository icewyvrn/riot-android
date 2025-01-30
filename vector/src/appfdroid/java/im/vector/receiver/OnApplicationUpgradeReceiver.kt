/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */

package im.vector.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import im.vector.services.EventStreamServiceX
import org.matrix.androidsdk.core.Log

class OnApplicationUpgradeReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(LOG_TAG, "## onReceive() : Application has been upgraded, restart event stream service.")
        EventStreamServiceX.onApplicationUpgrade(context)
    }

    companion object {
        private val LOG_TAG = OnApplicationUpgradeReceiver::class.java.simpleName
    }
}
