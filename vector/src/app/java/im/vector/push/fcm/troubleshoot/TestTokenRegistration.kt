/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */
package im.vector.push.fcm.troubleshoot

import androidx.fragment.app.Fragment
import im.vector.Matrix
import im.vector.R
import im.vector.VectorApp
import im.vector.fragments.troubleshoot.TroubleshootTest
import org.matrix.androidsdk.core.callback.ApiCallback
import org.matrix.androidsdk.core.model.MatrixError

/**
 * Force registration of the token to HomeServer
 */
class TestTokenRegistration(val fragment: Fragment) : TroubleshootTest(R.string.settings_troubleshoot_test_token_registration_title) {

    override fun perform() {
        Matrix.getInstance(VectorApp.getInstance().baseContext).pushManager.forceSessionsRegistration(object : ApiCallback<Void> {
            override fun onSuccess(info: Void?) {
                description = fragment.getString(R.string.settings_troubleshoot_test_token_registration_success)
                status = TestStatus.SUCCESS
            }

            override fun onNetworkError(e: Exception?) {
                description = fragment.getString(R.string.settings_troubleshoot_test_token_registration_failed, e?.localizedMessage)
                status = TestStatus.FAILED
            }

            override fun onMatrixError(e: MatrixError?) {
                description = fragment.getString(R.string.settings_troubleshoot_test_token_registration_failed, e?.localizedMessage)
                status = TestStatus.FAILED
            }

            override fun onUnexpectedError(e: Exception?) {
                description = fragment.getString(R.string.settings_troubleshoot_test_token_registration_failed, e?.localizedMessage)
                status = TestStatus.FAILED
            }
        })
    }

}