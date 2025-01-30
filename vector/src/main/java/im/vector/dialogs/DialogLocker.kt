/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */

package im.vector.dialogs

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import im.vector.activity.interfaces.Restorable
import org.matrix.androidsdk.core.Log

private const val KEY_DIALOG_IS_DISPLAYED = "DialogLocker.KEY_DIALOG_IS_DISPLAYED"
private const val LOG_TAG = "DialogLocker"

/**
 * Class to avoid displaying twice the same dialog
 */
class DialogLocker(savedInstanceState: Bundle?) : Restorable {

    private var isDialogDisplayed = savedInstanceState?.getBoolean(KEY_DIALOG_IS_DISPLAYED, false) == true

    private fun unlock() {
        isDialogDisplayed = false
    }

    private fun lock() {
        isDialogDisplayed = true
    }

    fun displayDialog(builder: () -> AlertDialog.Builder): AlertDialog? {
        return if (isDialogDisplayed) {
            Log.w(LOG_TAG, "Filtered dialog request")
            null
        } else {
            builder
                    .invoke()
                    .create()
                    .apply {
                        setOnShowListener { lock() }
                        setOnCancelListener { unlock() }
                        setOnDismissListener { unlock() }
                        show()
                    }
        }
    }

    override fun saveState(outState: Bundle) {
        outState.putBoolean(KEY_DIALOG_IS_DISPLAYED, isDialogDisplayed)
    }
}