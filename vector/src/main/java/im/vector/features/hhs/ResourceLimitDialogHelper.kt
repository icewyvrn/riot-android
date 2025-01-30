/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */

package im.vector.features.hhs

import android.app.Activity
import android.graphics.Typeface
import android.os.Bundle
import android.text.style.StyleSpan
import androidx.appcompat.app.AlertDialog
import com.binaryfork.spanny.Spanny
import im.vector.R
import im.vector.activity.interfaces.Restorable
import im.vector.dialogs.DialogLocker
import im.vector.util.openUri
import org.matrix.androidsdk.core.model.MatrixError

private const val LOG_TAG = "ResourceLimitDialogHelper"

class ResourceLimitDialogHelper private constructor(private val activity: Activity,
                                                    private val dialogLocker: DialogLocker) :

        Restorable by dialogLocker {

    constructor(activity: Activity, savedInstanceState: Bundle?) : this(activity, DialogLocker(savedInstanceState))

    private val formatter = ResourceLimitErrorFormatter(activity)

    /* ==========================================================================================
     * Public methods
     * ========================================================================================== */

    /**
     * Display the resource limit dialog, if not already displayed
     */
    fun displayDialog(matrixError: MatrixError) {
        dialogLocker.displayDialog {
            val title = Spanny(activity.getString(R.string.resource_limit_exceeded_title), StyleSpan(Typeface.BOLD))
            val message = formatter.format(matrixError, ResourceLimitErrorFormatter.Mode.Hard, separator = "\n\n")

            val builder = AlertDialog.Builder(activity, R.style.AppTheme_Dialog_Light)
                    .setTitle(title)
                    .setMessage(message)

            if (matrixError.adminUri != null) {
                builder
                        .setPositiveButton(R.string.resource_limit_contact_action) { _, _ ->
                            openUri(activity, matrixError.adminUri!!)
                        }
                        .setNegativeButton(R.string.cancel, null)

            } else {
                builder.setPositiveButton(R.string.ok, null)
            }

            builder
        }
    }

}