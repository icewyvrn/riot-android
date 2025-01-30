/*
 * Copyright 2019-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */
package im.vector.fragments.discovery

import android.view.View
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import butterknife.BindView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import im.vector.R
import im.vector.ui.epoxy.BaseEpoxyHolder
import im.vector.ui.util.setTextOrHide


@EpoxyModelClass(layout = R.layout.item_settings_helper_info)
abstract class SettingsInfoItem : EpoxyModelWithHolder<SettingsInfoItem.Holder>() {

    @EpoxyAttribute
    var helperText: String? = null

    @EpoxyAttribute
    @StringRes
    var helperTextResId: Int? = null


    @EpoxyAttribute
    var itemClickListener: View.OnClickListener? = null

    @EpoxyAttribute
    @DrawableRes
    var compoundDrawable: Int = R.drawable.vector_warning_red

    @EpoxyAttribute
    var showCompoundDrawable: Boolean = false

    override fun bind(holder: Holder) {
        super.bind(holder)

        if (helperTextResId != null) {
            holder.text.setText(helperTextResId!!)
        } else {
            holder.text.setTextOrHide(helperText)
        }

        holder.main.setOnClickListener(itemClickListener)

        if (showCompoundDrawable) {
            holder.text.setCompoundDrawablesWithIntrinsicBounds(compoundDrawable, 0, 0, 0)
        } else {
            holder.text.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        }
    }

    class Holder : BaseEpoxyHolder() {
        @BindView(R.id.settings_helper_text)
        lateinit var text: TextView
    }
}