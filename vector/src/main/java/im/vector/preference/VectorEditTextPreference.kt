/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */

package im.vector.preference

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceViewHolder
import im.vector.R
import org.matrix.androidsdk.core.Log

/**
 * Use this class to create an EditTextPreference form code and avoid a crash (see https://code.google.com/p/android/issues/detail?id=231576)
 */
class VectorEditTextPreference : EditTextPreference {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    init {
        dialogLayoutResource = R.layout.dialog_preference_edit_text
        isIconSpaceReserved = false
    }

    // No single line for title
    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        // display the title in multi-line to avoid ellipsis.
        try {
            holder.itemView.findViewById<TextView>(android.R.id.title)?.setSingleLine(false)
        } catch (e: Exception) {
            Log.e(LOG_TAG, "onBindView " + e.message, e)
        }

        super.onBindViewHolder(holder)
    }

    companion object {
        private val LOG_TAG = VectorEditTextPreference::class.java.simpleName
    }
}