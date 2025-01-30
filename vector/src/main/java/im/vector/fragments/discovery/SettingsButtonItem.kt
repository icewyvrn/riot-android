/*
 * Copyright 2019-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */
package im.vector.fragments.discovery

import android.view.View
import android.widget.Button
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import butterknife.BindView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import im.vector.R
import im.vector.ui.epoxy.BaseEpoxyHolder
import im.vector.ui.themes.ThemeUtils
import im.vector.ui.util.setTextOrHide


@EpoxyModelClass(layout = R.layout.item_settings_button)
abstract class SettingsButtonItem : EpoxyModelWithHolder<SettingsButtonItem.Holder>() {


    @EpoxyAttribute
    var buttonTitle: String? = null

    @EpoxyAttribute
    @StringRes
    var buttonTitleId: Int? = null

    @EpoxyAttribute
    var buttonStyle: SettingsTextButtonItem.ButtonStyle = SettingsTextButtonItem.ButtonStyle.POSITIVE

    @EpoxyAttribute
    var buttonClickListener: View.OnClickListener? = null

    override fun bind(holder: Holder) {
        super.bind(holder)
        if (buttonTitleId != null) {
            holder.button.setText(buttonTitleId!!)
        } else {
            holder.button.setTextOrHide(buttonTitle)
        }

        when (buttonStyle) {
            SettingsTextButtonItem.ButtonStyle.POSITIVE    -> {
                holder.button.setTextColor(ThemeUtils.getColor(holder.main.context, R.attr.colorAccent))
            }
            SettingsTextButtonItem.ButtonStyle.DESTRUCTIVE -> {
                holder.button.setTextColor(ContextCompat.getColor(holder.main.context, R.color.vector_error_color))
            }
        }

        holder.button.setOnClickListener(buttonClickListener)
    }

    class Holder : BaseEpoxyHolder() {
        @BindView(R.id.settings_item_button)
        lateinit var button: Button
    }
}