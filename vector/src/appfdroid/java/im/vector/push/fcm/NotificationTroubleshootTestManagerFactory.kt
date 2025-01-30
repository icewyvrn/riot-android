/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */
package im.vector.push.fcm

import androidx.fragment.app.Fragment
import im.vector.fragments.troubleshoot.*
import im.vector.push.fcm.troubleshoot.TestAutoStartBoot
import im.vector.push.fcm.troubleshoot.TestBackgroundRestrictions
import org.matrix.androidsdk.MXSession

class NotificationTroubleshootTestManagerFactory {

    companion object {
        fun createTestManager(fragment: Fragment, session: MXSession?): NotificationTroubleshootTestManager {
            val mgr = NotificationTroubleshootTestManager(fragment)
            mgr.addTest(TestSystemSettings(fragment))
            if (session != null) {
                mgr.addTest(TestAccountSettings(fragment, session))
            }
            mgr.addTest(TestDeviceSettings(fragment))
            if (session != null) {
                mgr.addTest(TestBingRulesSettings(fragment, session))
            }
            // mgr.addTest(TestNotificationServiceRunning(fragment))
            // mgr.addTest(TestServiceRestart(fragment))
            mgr.addTest(TestAutoStartBoot(fragment))
            mgr.addTest(TestBackgroundRestrictions(fragment))
            // mgr.addTest(TestBatteryOptimization(fragment))
            return mgr
        }
    }

}