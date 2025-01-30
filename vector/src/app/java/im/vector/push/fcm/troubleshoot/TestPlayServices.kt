/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */
package im.vector.push.fcm.troubleshoot

import androidx.fragment.app.Fragment
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import im.vector.R
import im.vector.fragments.troubleshoot.TroubleshootTest
import org.matrix.androidsdk.core.Log

/*
* Check that the play services APK is available an up-to-date. If needed provide quick fix to install it.
 */
class TestPlayServices(val fragment: Fragment) : TroubleshootTest(R.string.settings_troubleshoot_test_play_services_title) {

    override fun perform() {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = apiAvailability.isGooglePlayServicesAvailable(fragment.context)
        if (resultCode == ConnectionResult.SUCCESS) {
            quickFix = null
            description = fragment.getString(R.string.settings_troubleshoot_test_play_services_success)
            status = TestStatus.SUCCESS
        } else {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                quickFix = object : TroubleshootQuickFix(R.string.settings_troubleshoot_test_play_services_quickfix) {
                    override fun doFix() {
                        fragment.activity?.let {
                            apiAvailability.getErrorDialog(it, resultCode, 9000 /*hey does the magic number*/).show()
                        }
                    }
                }
                Log.e(this::javaClass.name, "Play Services apk error $resultCode -> ${apiAvailability.getErrorString(resultCode)}.")
            }

            description = fragment.getString(R.string.settings_troubleshoot_test_play_services_failed, apiAvailability.getErrorString(resultCode))
            status = TestStatus.FAILED
        }
    }

}

