/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package im.vector.dialogs

import android.content.Context

internal class DialogCallAdapter(context: Context) : DialogAdapter(context) {

    init {
        add(DialogListItem.StartVoiceCall)
        add(DialogListItem.StartVideoCall)
    }
}
