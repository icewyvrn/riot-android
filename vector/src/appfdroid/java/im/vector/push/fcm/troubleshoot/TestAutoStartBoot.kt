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
import im.vector.util.PreferencesManager

/**
 * Test that the application is started on boot
 */
class TestAutoStartBoot(val fragment: Fragment) : TroubleshootTest(R.string.settings_troubleshoot_test_service_boot_title) {

    override fun perform() {
        if (PreferencesManager.autoStartOnBoot(fragment.context)) {
            description = fragment.getString(R.string.settings_troubleshoot_test_service_boot_success)
            status = TestStatus.SUCCESS
            quickFix = null
        } else {
            description = fragment.getString(R.string.settings_troubleshoot_test_service_boot_failed)
            quickFix = object : TroubleshootQuickFix(R.string.settings_troubleshoot_test_service_boot_quickfix) {
                override fun doFix() {
                    PreferencesManager.setAutoStartOnBoot(fragment.context, true)
                    manager?.retry()
                }
            }
            status = TestStatus.FAILED
        }
    }
}