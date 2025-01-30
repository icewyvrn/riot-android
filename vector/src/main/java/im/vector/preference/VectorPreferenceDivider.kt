/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */

package im.vector.preference

import android.content.Context
import android.util.AttributeSet
import androidx.preference.Preference
import im.vector.R

/**
 * Divider for Preference screen
 */
class VectorPreferenceDivider @JvmOverloads constructor(context: Context,
                                                        attrs: AttributeSet? = null,
                                                        defStyleAttr: Int = 0,
                                                        defStyleRes: Int = 0
) : Preference(context, attrs, defStyleAttr, defStyleRes) {

    init {
        layoutResource = R.layout.vector_preference_divider
    }
}