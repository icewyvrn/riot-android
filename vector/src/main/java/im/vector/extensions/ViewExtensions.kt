/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */

package im.vector.extensions

import android.text.InputType
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import im.vector.R

/**
 * Remove left margin of a SearchView
 */
fun SearchView.withoutLeftMargin() {
    (findViewById<View>(R.id.search_edit_frame))?.let {
        val searchEditFrameParams = it.layoutParams as ViewGroup.MarginLayoutParams
        searchEditFrameParams.leftMargin = 0
        it.layoutParams = searchEditFrameParams
    }

    (findViewById<View>(R.id.search_mag_icon))?.let {
        val searchIconParams = it.layoutParams as ViewGroup.MarginLayoutParams
        searchIconParams.leftMargin = 0
        it.layoutParams = searchIconParams
    }
}

fun EditText.showPassword(visible: Boolean, updateCursor: Boolean = true) {
    if (visible) {
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
    } else {
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
    }
    if (updateCursor) setSelection(text?.length ?: 0)
}