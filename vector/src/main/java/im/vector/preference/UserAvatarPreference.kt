/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */

package im.vector.preference

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import im.vector.R
import im.vector.util.VectorUtils
import org.matrix.androidsdk.MXSession

open class UserAvatarPreference : Preference {

    internal var mAvatarView: ImageView? = null
    internal var mSession: MXSession? = null
    private var mLoadingProgressBar: ProgressBar? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    init {
        widgetLayoutResource = R.layout.vector_settings_round_avatar
        isIconSpaceReserved = false
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)

        mAvatarView = holder.itemView.findViewById(R.id.settings_avatar)
        mLoadingProgressBar = holder.itemView.findViewById(R.id.avatar_update_progress_bar)
        refreshAvatar()
    }

    open fun refreshAvatar() {
        if (null != mAvatarView && null != mSession) {
            val myUser = mSession!!.myUser
            VectorUtils.loadUserAvatar(context, mSession, mAvatarView, myUser.avatarUrl, myUser.user_id, myUser.displayname)
        }
    }

    fun setSession(session: MXSession) {
        mSession = session
        refreshAvatar()
    }
}