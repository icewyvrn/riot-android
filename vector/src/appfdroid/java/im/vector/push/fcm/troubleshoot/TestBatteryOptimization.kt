/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */
package im.vector.push.fcm.troubleshoot

import androidx.fragment.app.Fragment
import im.vector.R
import im.vector.fragments.troubleshoot.NotificationTroubleshootTestManager
import im.vector.fragments.troubleshoot.TroubleshootTest
import im.vector.util.isIgnoringBatteryOptimizations
import im.vector.util.requestDisablingBatteryOptimization

// Not used anymore
class TestBatteryOptimization(val fragment: Fragment) : TroubleshootTest(R.string.settings_troubleshoot_test_battery_title) {

    override fun perform() {

        if (fragment.context != null && isIgnoringBatteryOptimizations(fragment.context!!)) {
            description = fragment.getString(R.string.settings_troubleshoot_test_battery_success)
            status = TestStatus.SUCCESS
            quickFix = null
        } else {
            description = fragment.getString(R.string.settings_troubleshoot_test_battery_failed)
            quickFix = object : TroubleshootQuickFix(R.string.settings_troubleshoot_test_battery_quickfix) {
                override fun doFix() {
                    fragment.activity?.let {
                        requestDisablingBatteryOptimization(it, fragment, NotificationTroubleshootTestManager.REQ_CODE_FIX)
                    }
                }
            }
            status = TestStatus.FAILED
        }
    }

}