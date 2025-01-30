/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */

package im.vector.notifications

/**
 * Data class to hold information about a group of notifications for a room
 */
data class RoomEventGroupInfo(
        val roomId: String
) {
    var roomDisplayName: String = ""
    var roomAvatarPath: String? = null
    //An event in the list has not yet been display
    var hasNewEvent: Boolean = false
    //true if at least one on the not yet displayed event is noisy
    var shouldBing: Boolean = false
    var customSound: String? = null
    var hasSmartReplyError = false
    var isDirect = false
}