/*
 * Copyright 2025 New Vector Ltd.
 * Copyright 2017 Vector Creations Ltd
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package im.vector.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.rest.model.group.GroupRoom;

import butterknife.BindView;
import butterknife.ButterKnife;
import im.vector.R;
import im.vector.util.VectorUtils;

public class GroupRoomViewHolder extends RecyclerView.ViewHolder {
    private static final String LOG_TAG = GroupRoomViewHolder.class.getSimpleName();

    @BindView(R.id.adapter_item_group_contact_avatar)
    ImageView vContactAvatar;

    @BindView(R.id.contact_name)
    TextView vContactName;

    @Nullable
    @BindView(R.id.contact_desc)
    TextView vContactDesc;

    public GroupRoomViewHolder(final View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    /**
     * Refresh the holder layout
     *
     * @param context   the context
     * @param session   the session
     * @param groupRoom the group room
     */
    public void populateViews(final Context context, final MXSession session, final GroupRoom groupRoom) {
        // sanity check
        if (null == groupRoom) {
            Log.e(LOG_TAG, "## populateViews() : null groupRoom");
            return;
        }

        if (null == session) {
            Log.e(LOG_TAG, "## populateViews() : null session");
            return;
        }

        if (null == session.getDataHandler()) {
            Log.e(LOG_TAG, "## populateViews() : null dataHandler");
            return;
        }

        vContactName.setText(groupRoom.getDisplayName());
        VectorUtils.loadUserAvatar(context, session, vContactAvatar, groupRoom.avatarUrl, groupRoom.roomId, groupRoom.getDisplayName());

        if (null != vContactDesc) {
            vContactDesc.setText(groupRoom.topic);
        }
    }
}
