/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */
package im.vector.push.fcm

import androidx.fragment.app.Fragment
import im.vector.fragments.troubleshoot.*
import im.vector.push.fcm.troubleshoot.TestFirebaseToken
import im.vector.push.fcm.troubleshoot.TestPlayServices
import im.vector.push.fcm.troubleshoot.TestTokenRegistration
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
            mgr.addTest(TestPlayServices(fragment))
            mgr.addTest(TestFirebaseToken(fragment))
            mgr.addTest(TestTokenRegistration(fragment))
            return mgr
        }
    }

}