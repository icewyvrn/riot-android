/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */
package im.vector.push.fcm.troubleshoot

import androidx.fragment.app.Fragment
import im.vector.R
import im.vector.fragments.troubleshoot.TroubleshootTest
import im.vector.services.EventStreamService

// Not used anymore
class TestNotificationServiceRunning(val fragment: Fragment) : TroubleshootTest(R.string.settings_troubleshoot_test_foreground_service_started_title) {

    override fun perform() {
        if (EventStreamService.isStopped()) {
            description = fragment.getString(R.string.settings_troubleshoot_test_foreground_service_started_failed)
            status = TestStatus.FAILED
            quickFix = null
        } else {
            description = fragment.getString(R.string.settings_troubleshoot_test_foreground_service_startedt_success)
            quickFix = null
            status = TestStatus.SUCCESS
        }
    }

}