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
import androidx.preference.PreferenceViewHolder
import androidx.preference.SwitchPreference

/**
 * Switch preference with title on multiline (only used in XML)
 */
class VectorSwitchPreference : SwitchPreference {

    // Note: @JvmOverload does not work here...
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context) : super(context)

    init {
        isIconSpaceReserved = false
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        // display the title in multi-line to avoid ellipsis.
        holder.itemView.findViewById<TextView>(android.R.id.title)?.setSingleLine(false)

        super.onBindViewHolder(holder)
    }
}
