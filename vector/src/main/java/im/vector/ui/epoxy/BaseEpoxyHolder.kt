/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package im.vector.ui.epoxy

import android.view.View
import androidx.annotation.CallSuper
import butterknife.ButterKnife
import com.airbnb.epoxy.EpoxyHolder


/**
 * This class ensure butterknife is used to bind Views
 */
abstract class BaseEpoxyHolder : EpoxyHolder() {
    lateinit var main: View

    @CallSuper
    override fun bindView(itemView: View) {
        main = itemView

        ButterKnife.bind(this, itemView)
    }
}