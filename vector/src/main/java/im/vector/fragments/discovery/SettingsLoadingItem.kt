/*
 * Copyright 2019-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */
package im.vector.fragments.discovery

import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import butterknife.BindView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import im.vector.R
import im.vector.ui.epoxy.BaseEpoxyHolder
import im.vector.ui.util.setTextOrHide


@EpoxyModelClass(layout = R.layout.item_loading)
abstract class SettingsLoadingItem : EpoxyModelWithHolder<SettingsLoadingItem.Holder>() {

    @EpoxyAttribute var loadingText: String? = null

    override fun bind(holder: Holder) {
        super.bind(holder)
        holder.textView.setTextOrHide(loadingText)
        holder.progressBar.isVisible = true
        holder.progressBar.animate()
    }


    class Holder : BaseEpoxyHolder() {
        @BindView(R.id.loadingText)
        lateinit var textView: TextView

        @BindView(R.id.loadingProgress)
        lateinit var progressBar: ProgressBar
    }


}
