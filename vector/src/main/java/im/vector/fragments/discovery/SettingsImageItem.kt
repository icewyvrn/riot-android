/*
 * Copyright 2019-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */
package im.vector.fragments.discovery

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import butterknife.BindView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import im.vector.R
import im.vector.ui.epoxy.BaseEpoxyHolder
import im.vector.ui.util.setTextOrHide


@EpoxyModelClass(layout = R.layout.item_settings_radio_single_line)
abstract class SettingsImageItem : EpoxyModelWithHolder<SettingsImageItem.Holder>() {

    @EpoxyAttribute
    var title: String? = null

    @EpoxyAttribute
    @StringRes
    var titleResId: Int? = null

    @EpoxyAttribute
    @DrawableRes
    var endIconResourceId: Int = -1

    @EpoxyAttribute
    var itemClickListener: View.OnClickListener? = null

    override fun bind(holder: Holder) {
        super.bind(holder)

        if (titleResId != null) {
            holder.textView.setText(titleResId!!)
        } else {
            holder.textView.setTextOrHide(title)
        }
        if (endIconResourceId != -1) {
            holder.accessoryImage.setImageResource(endIconResourceId)
            holder.accessoryImage.isVisible = true
        } else {
            holder.accessoryImage.isVisible = false
        }

        holder.main.setOnClickListener(itemClickListener)
    }

    class Holder : BaseEpoxyHolder() {

        @BindView(R.id.settings_item_text)
        lateinit var textView: TextView

        @BindView(R.id.settings_item_image)
        lateinit var accessoryImage: ImageView
    }
}