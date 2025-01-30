/*
 * Copyright 2019-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */
package im.vector.notifications

import androidx.core.app.NotificationCompat

data class InviteNotifiableEvent(
        override var matrixID: String?,
        override val eventId: String,
        var roomId: String,
        override var noisy: Boolean,
        override val title: String,
        override val description: String,
        override val type: String?,
        override val timestamp: Long,
        override var soundName: String?,
        override var isPushGatewayEvent: Boolean = false) : NotifiableEvent {

    override var hasBeenDisplayed: Boolean = false
    override var lockScreenVisibility = NotificationCompat.VISIBILITY_PUBLIC

}