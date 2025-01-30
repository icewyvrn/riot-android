/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */
package im.vector.push.fcm.troubleshoot

import android.app.ActivityManager
import android.content.Context
import androidx.fragment.app.Fragment
import im.vector.R
import im.vector.VectorApp
import im.vector.fragments.troubleshoot.TroubleshootTest
import im.vector.services.EventStreamService
import java.util.*
import kotlin.concurrent.timerTask


/**
 * Stop the event stream service and check that it is restarted
 */
// Not used anymore
class TestServiceRestart(val fragment: Fragment) : TroubleshootTest(R.string.settings_troubleshoot_test_service_restart_title) {

    var timer: Timer? = null

    override fun perform() {
        status = TestStatus.RUNNING
        EventStreamService.getInstance()?.stopSelf()
        timer = Timer()
        timer?.schedule(timerTask {
            if (isMyServiceRunning(EventStreamService::class.java)) {
                fragment.activity?.runOnUiThread {
                    description = fragment.getString(R.string.settings_troubleshoot_test_service_restart_success)
                    quickFix = null
                    status = TestStatus.SUCCESS
                }
                timer?.cancel()
            }
        }, 0, 1000)

        timer?.schedule(timerTask {
            fragment.activity?.runOnUiThread {
                status = TestStatus.FAILED
                description = fragment.getString(R.string.settings_troubleshoot_test_service_restart_failed)
            }
            timer?.cancel()
        }, 15000)
    }


    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = VectorApp.getInstance().baseContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    override fun cancel() {
        super.cancel()
        timer?.cancel()
    }
}