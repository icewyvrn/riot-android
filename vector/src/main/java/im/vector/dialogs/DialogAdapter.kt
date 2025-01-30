/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package im.vector.dialogs

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import im.vector.R

internal abstract class DialogAdapter(context: Context) :
        ArrayAdapter<DialogListItem>(context, R.layout.adapter_item_dialog, R.id.adapter_item_dialog_text) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)

        if (view.tag == null) {
            view.tag = DialogListItemHolder(view)
        }

        (view.tag as DialogListItemHolder).let {
            it.icon.setImageResource(getItem(position).iconRes)
            it.text.setText(getItem(position).titleRes)
        }

        return view
    }

}
