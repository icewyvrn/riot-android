/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */

package im.vector.dialogs

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import im.vector.R

class DialogListItemHolder(view: View) {
    @BindView(R.id.adapter_item_dialog_icon)
    lateinit var icon: ImageView

    @BindView(R.id.adapter_item_dialog_text)
    lateinit var text: TextView

    init {
        ButterKnife.bind(this, view)
    }
}