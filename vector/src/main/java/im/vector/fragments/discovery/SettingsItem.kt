/*
 * Copyright 2019-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */
package im.vector.fragments.discovery

import android.view.View
import android.widget.Switch
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import butterknife.BindView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import im.vector.R
import im.vector.ui.epoxy.BaseEpoxyHolder
import im.vector.ui.themes.ThemeUtils
import im.vector.ui.util.setTextOrHide

@EpoxyModelClass(layout = R.layout.item_settings_simple_item)
abstract class SettingsItem : EpoxyModelWithHolder<SettingsItem.Holder>() {

    @EpoxyAttribute
    var title: String? = null

    @EpoxyAttribute
    @StringRes
    var titleResId: Int? = null

    @EpoxyAttribute
    @StringRes
    var descriptionResId: Int? = null

    @EpoxyAttribute
    var description: CharSequence? = null

    @EpoxyAttribute
    var itemClickListener: View.OnClickListener? = null

    override fun bind(holder: Holder) {

        if (titleResId != null) {
            holder.titleText.setText(titleResId!!)
        } else {
            holder.titleText.setTextOrHide(title)
        }

        if (descriptionResId != null) {
            holder.descriptionText.setText(descriptionResId!!)
        } else {
            holder.descriptionText.setTextOrHide(description)
        }

        //If there is only a description, use primary color
//        holder.descriptionText.setTextColor(
//                if (holder.titleText.text.isNullOrBlank()) {
//                    ThemeUtils.getColor(holder.main.context, android.R.attr.textColorPrimary)
//                } else {
//                    ThemeUtils.getColor(holder.main.context, android.R.attr.textColorSecondary)
//                }
//        )

        holder.switchButton.isVisible = false

        holder.main.setOnClickListener(itemClickListener)
    }

    class Holder : BaseEpoxyHolder() {

        @BindView(R.id.settings_item_title)
        lateinit var titleText: TextView

        @BindView(R.id.settings_item_description)
        lateinit var descriptionText: TextView

        @BindView(R.id.settings_item_switch)
        lateinit var switchButton: Switch
    }
}