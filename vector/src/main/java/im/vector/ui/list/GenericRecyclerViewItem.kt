/*
 * Copyright 2019-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */
package im.vector.ui.list

import androidx.annotation.DrawableRes

/**
 * A generic list item.
 * Displays an item with a title, and optional description.
 * Can display an accessory on the right, that can be an image or an indeterminate progress.
 * If provided with an action, will display a button at the bottom of the list item.
 */
class GenericRecyclerViewItem(val title: String,
                              var description: String? = null,
                              val style: STYLE = STYLE.NORMAL_TEXT) {

    enum class STYLE {
        BIG_TEXT,
        NORMAL_TEXT
    }

    @DrawableRes
    var endIconResourceId: Int = -1

    var hasIndeterminateProcess = false

    var buttonAction: Action? = null

    var itemClickAction: Action? = null

    class Action(var title: String) {
        var perform: Runnable? = null
    }
}