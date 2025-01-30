/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package im.vector.preference

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.preference.PreferenceViewHolder
import im.vector.R

/**
 * Preference used in Room setting for Room aliases
 */
class AddressPreference : VectorPreference {

    // members
    private var mMainAddressIconView: ImageView? = null
    private var mIsMainIconVisible = false

    /**
     * @return the main icon view.
     */
    val mainIconView: View?
        get() = mMainAddressIconView

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    init {
        widgetLayoutResource = R.layout.vector_settings_address_preference
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)

        val view = holder.itemView
        mMainAddressIconView = view.findViewById(R.id.main_address_icon_view)
        mMainAddressIconView!!.visibility = if (mIsMainIconVisible) View.VISIBLE else View.GONE
    }

    /**
     * Set the main address icon visibility.
     *
     * @param isVisible true to display the main icon
     */
    fun setMainIconVisible(isVisible: Boolean) {
        mIsMainIconVisible = isVisible

        mMainAddressIconView?.visibility = if (mIsMainIconVisible) View.VISIBLE else View.GONE
    }
}