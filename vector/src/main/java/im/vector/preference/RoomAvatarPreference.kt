/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package im.vector.preference

import android.content.Context
import android.util.AttributeSet
import im.vector.util.VectorUtils
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.data.Room

/**
 * Specialized class to target a Room avatar preference.
 * Based don the avatar preference class it redefines refreshAvatar() and
 * add the new method  setConfiguration().
 */
class RoomAvatarPreference : UserAvatarPreference {

    private var mRoom: Room? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    override fun refreshAvatar() {
        if (null != mAvatarView && null != mRoom) {
            VectorUtils.loadRoomAvatar(context, mSession, mAvatarView, mRoom)
        }
    }

    fun setConfiguration(aSession: MXSession, aRoom: Room) {
        mSession = aSession
        mRoom = aRoom
        refreshAvatar()
    }
}