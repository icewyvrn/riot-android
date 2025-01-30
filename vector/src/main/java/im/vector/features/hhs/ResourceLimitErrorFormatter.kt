/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */

package im.vector.features.hhs

import android.content.Context
import android.text.Html
import androidx.annotation.StringRes
import com.binaryfork.spanny.Spanny
import im.vector.R
import org.matrix.androidsdk.core.model.MatrixError

class ResourceLimitErrorFormatter(private val context: Context) {

    // 'hard' if the logged in user has been locked out, 'soft' if they haven't
    sealed class Mode(@StringRes val mauErrorRes: Int, @StringRes val defaultErrorRes: Int, @StringRes val contactRes: Int) {
        // User can still send message (will be used in a near future)
        object Soft : Mode(R.string.resource_limit_soft_mau, R.string.resource_limit_soft_default, R.string.resource_limit_soft_contact)

        // User cannot send message anymore
        object Hard : Mode(R.string.resource_limit_hard_mau, R.string.resource_limit_hard_default, R.string.resource_limit_hard_contact)
    }

    fun format(matrixError: MatrixError,
               mode: Mode,
               separator: CharSequence = " ",
               clickable: Boolean = false): CharSequence {
        val error = if (MatrixError.LIMIT_TYPE_MAU == matrixError.limitType) {
            context.getString(mode.mauErrorRes)
        } else {
            context.getString(mode.defaultErrorRes)
        }
        val contact = if (clickable && matrixError.adminUri != null) {
            val contactSubString = uriAsLink(matrixError.adminUri!!)
            val contactFullString = context.getString(mode.contactRes, contactSubString)
            Html.fromHtml(contactFullString)
        } else {
            val contactSubString = context.getString(R.string.resource_limit_contact_admin)
            context.getString(mode.contactRes, contactSubString)
        }
        return Spanny(error)
                .append(separator)
                .append(contact)
    }

    /**
     * Create a HTML link with a uri
     */
    private fun uriAsLink(uri: String): String {
        val contactStr = context.getString(R.string.resource_limit_contact_admin)
        return "<a href=\"$uri\">$contactStr</a>"
    }
}