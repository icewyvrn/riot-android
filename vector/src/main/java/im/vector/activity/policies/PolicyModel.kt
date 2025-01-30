/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package im.vector.activity.policies

import android.view.View
import android.widget.CompoundButton
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import im.vector.R

@EpoxyModelClass(layout = R.layout.adapter_item_policy)
abstract class PolicyModel : EpoxyModelWithHolder<PolicyHolder>() {
    @EpoxyAttribute
    var checked: Boolean = false

    @EpoxyAttribute
    var title: String? = null

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var checkChangeListener: CompoundButton.OnCheckedChangeListener? = null

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var clickListener: View.OnClickListener? = null

    override fun bind(holder: PolicyHolder) {
        holder.let {
            it.checkbox.isChecked = checked
            it.checkbox.setOnCheckedChangeListener(checkChangeListener)
            it.title.text = title
            it.main.setOnClickListener(clickListener)
        }
    }

    // Ensure checkbox behaves as expected (remove the listener)
    override fun unbind(holder: PolicyHolder) {
        super.unbind(holder)
        holder.checkbox.setOnCheckedChangeListener(null)
    }
}