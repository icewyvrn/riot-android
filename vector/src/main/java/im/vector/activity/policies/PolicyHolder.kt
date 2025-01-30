/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */

package im.vector.activity.policies

import android.widget.CheckBox
import android.widget.TextView
import butterknife.BindView
import im.vector.R
import im.vector.ui.epoxy.BaseEpoxyHolder

class PolicyHolder : BaseEpoxyHolder() {

    @BindView(R.id.adapter_item_policy_checkbox)
    lateinit var checkbox: CheckBox

    @BindView(R.id.adapter_item_policy_title)
    lateinit var title: TextView

}