/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */
package im.vector.fragments.troubleshoot

import androidx.fragment.app.Fragment
import im.vector.Matrix
import im.vector.R

/**
 * Checks if notifications are enable in the system settings for this app.
 */
class TestDeviceSettings(val fragment: Fragment) : TroubleshootTest(R.string.settings_troubleshoot_test_device_settings_title) {

    override fun perform() {
        val pushManager = Matrix.getInstance(fragment.activity).pushManager
        if (pushManager.areDeviceNotificationsAllowed()) {
            description = fragment.getString(R.string.settings_troubleshoot_test_device_settings_success)
            quickFix = null
            status = TestStatus.SUCCESS
        } else {
            quickFix = object : TroubleshootQuickFix(R.string.settings_troubleshoot_test_device_settings_quickfix) {
                override fun doFix() {
                    pushManager.setDeviceNotificationsAllowed(true)
                    manager?.retry()
                }

            }
            description = fragment.getString(R.string.settings_troubleshoot_test_device_settings_failed)
            status = TestStatus.FAILED
        }
    }
}