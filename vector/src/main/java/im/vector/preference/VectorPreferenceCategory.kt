/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package im.vector.preference

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.TextView
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceViewHolder

/**
 * Customize PreferenceCategory class to redefine some attributes.
 */
class VectorPreferenceCategory : PreferenceCategory {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        isIconSpaceReserved = false
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)

        val titleTextView = holder.itemView.findViewById<TextView>(android.R.id.title)

        titleTextView?.setTypeface(null, Typeface.BOLD)

        // "isIconSpaceReserved = false" does not work for preference category, so remove the padding
        (titleTextView?.parent as? ViewGroup)?.setPadding(0, 0, 0, 0)
    }
}