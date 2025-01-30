/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */

package im.vector.extensions

import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.crypto.data.MXDeviceInfo
import org.matrix.androidsdk.data.Room
import org.matrix.androidsdk.rest.model.Event
import kotlin.math.max

/* ==========================================================================================
 * MXDeviceInfo
 * ========================================================================================== */

fun MXDeviceInfo.getFingerprintHumanReadable() = fingerprint()
        ?.chunked(4)
        ?.joinToString(separator = " ")


/* ==========================================================================================
 * Room
 * ========================================================================================== */

/**
 * Helper method to retrieve the max power level contained in the room.
 * This value is used to indicate what is the power level value required
 * to be admin of the room.
 *
 * @return max power level of the current room
 */
fun Room?.getRoomMaxPowerLevel(): Int {
    if (this == null) {
        return 0
    }

    var maxPowerLevel = 0

    state?.powerLevels?.let {
        maxPowerLevel = max(it.users_default, it.users?.values?.max() ?: 0)
    }

    return maxPowerLevel
}

/**
 * Check if the user power level allows to update the room avatar. This is mainly used to
 * determine if camera permission must be checked or not.
 *
 * @param aSession the session
 * @return true if the user power level allows to update the avatar, false otherwise.
 */
fun Room.isPowerLevelEnoughForAvatarUpdate(aSession: MXSession?): Boolean {
    var canUpdateAvatarWithCamera = false

    if (null != aSession) {
        state.powerLevels?.let {
            val powerLevel = it.getUserPowerLevel(aSession.myUserId)

            // check the power level against avatar level
            canUpdateAvatarWithCamera = powerLevel >= it.minimumPowerLevelForSendingEventAsStateEvent(Event.EVENT_TYPE_STATE_ROOM_AVATAR)
        }
    }

    return canUpdateAvatarWithCamera
}

/* ==========================================================================================
 * Event
 * ========================================================================================== */

fun Event.getSessionId() = wireContent
        ?.takeIf { it.isJsonObject }
        ?.asJsonObject
        ?.get("session_id")
        ?.asString