/*
 * Copyright 2019-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package im.vector.fragments.terms

import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import butterknife.BindView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import im.vector.R
import im.vector.ui.epoxy.BaseEpoxyHolder

@EpoxyModelClass(layout = R.layout.item_tos)
abstract class TermsModel : EpoxyModelWithHolder<TermsModel.Holder>() {

    @EpoxyAttribute
    var checked: Boolean = false

    @EpoxyAttribute
    var name: String? = null

    @EpoxyAttribute
    var description: String? = null

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var checkChangeListener: CompoundButton.OnCheckedChangeListener? = null

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var clickListener: View.OnClickListener? = null

    override fun bind(holder: Holder) {
        holder.checkbox.isChecked = checked
        holder.title.text = name
        holder.description.text = description
        holder.checkbox.setOnCheckedChangeListener(checkChangeListener)
        holder.main.setOnClickListener(clickListener)
    }

    class Holder : BaseEpoxyHolder() {
        @BindView(R.id.term_accept_checkbox)
        lateinit var checkbox: CheckBox

        @BindView(R.id.term_name)
        lateinit var title: TextView

        @BindView(R.id.term_description)
        lateinit var description: TextView
    }
}