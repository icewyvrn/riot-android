/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */
package im.vector.fragments.troubleshoot

import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import im.vector.R
import im.vector.util.startNotificationSettingsIntent

/**
 * Checks if notifications are enable in the system settings for this app.
 */
class TestSystemSettings(val fragment: Fragment) : TroubleshootTest(R.string.settings_troubleshoot_test_system_settings_title) {

    override fun perform() {
        if (NotificationManagerCompat.from(fragment.context!!).areNotificationsEnabled()) {
            description = fragment.getString(R.string.settings_troubleshoot_test_system_settings_success)
            quickFix = null
            status = TestStatus.SUCCESS
        } else {
            description = fragment.getString(R.string.settings_troubleshoot_test_system_settings_failed)
            quickFix = object : TroubleshootQuickFix(R.string.open_settings) {
                override fun doFix() {
                    if (manager?.diagStatus == TestStatus.RUNNING) return //wait before all is finished
                    startNotificationSettingsIntent(fragment, NotificationTroubleshootTestManager.REQ_CODE_FIX)
                }

            }
            status = TestStatus.FAILED
        }
    }
}