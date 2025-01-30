/*
 * Copyright 2019-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */
package im.vector.notifications

import java.io.Serializable

interface NotifiableEvent : Serializable {
    var matrixID: String?
    val eventId: String
    var noisy: Boolean
    val title: String
    val description: String?
    val type: String?
    val timestamp: Long
    //NotificationCompat.VISIBILITY_PUBLIC , VISIBILITY_PRIVATE , VISIBILITY_SECRET
    var lockScreenVisibility: Int
    // Compat: Only for android <7, for newer version the sound is defined in the channel
    var soundName: String?
    var hasBeenDisplayed: Boolean
    //Used to know if event should be replaced with the one coming from eventstream
    var isPushGatewayEvent : Boolean
}

