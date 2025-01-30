/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */

package im.vector.dialogs

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import im.vector.Matrix
import im.vector.R
import im.vector.activity.VectorWebViewActivity
import im.vector.activity.interfaces.Restorable
import im.vector.webview.WebViewMode
import org.matrix.androidsdk.core.Log
import org.matrix.androidsdk.core.model.MatrixError

private const val LOG_TAG = "ConsentNotGivenHelper"

class ConsentNotGivenHelper private constructor(private val activity: Activity,
                                                private val dialogLocker: DialogLocker) :
        Restorable by dialogLocker {

    constructor(activity: Activity, savedInstanceState: Bundle?) : this(activity, DialogLocker(savedInstanceState))

    /* ==========================================================================================
     * Public methods
     * ========================================================================================== */

    /**
     * Display the consent dialog, if not already displayed
     */
    fun displayDialog(matrixError: MatrixError) {
        if (matrixError.consentUri == null) {
            Log.e(LOG_TAG, "Missing required parameter 'consent_uri'")
            return
        }
        dialogLocker.displayDialog {
            AlertDialog.Builder(activity)
                    .setTitle(R.string.settings_app_term_conditions)
                    .setMessage(activity.getString(R.string.dialog_user_consent_content,
                            Matrix.getInstance(activity).defaultSession.homeServerConfig.homeserverUri.host))
                    .setPositiveButton(R.string.dialog_user_consent_submit) { _, _ ->
                        openWebViewActivity(matrixError.consentUri)
                    }
        }
    }

    /* ==========================================================================================
     * Private
     * ========================================================================================== */

    private fun openWebViewActivity(consentUri: String) {
        val intent = VectorWebViewActivity.getIntent(activity, consentUri, activity.getString(R.string.settings_app_term_conditions), WebViewMode.CONSENT)
        activity.startActivity(intent)
    }
}