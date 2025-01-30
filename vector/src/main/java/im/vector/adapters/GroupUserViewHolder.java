/*
 * Copyright 2025 New Vector Ltd.
 * Copyright 2017 Vector Creations Ltd
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */

package im.vector.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.rest.model.group.GroupUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import im.vector.R;
import im.vector.util.VectorUtils;

public class GroupUserViewHolder extends RecyclerView.ViewHolder {
    private static final String LOG_TAG = GroupUserViewHolder.class.getSimpleName();

    @BindView(R.id.adapter_item_group_contact_avatar)
    ImageView vContactAvatar;

    @BindView(R.id.contact_name)
    TextView vContactName;

    public GroupUserViewHolder(final View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    /**
     * Refresh the holder layout
     *
     * @param context   the context
     * @param session   the session
     * @param groupUser the user
     */
    public void populateViews(final Context context, final MXSession session, final GroupUser groupUser) {
        // sanity check
        if (null == groupUser) {
            Log.e(LOG_TAG, "## populateViews() : null groupUser");
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

        vContactName.setText(groupUser.getDisplayname());
        VectorUtils.loadUserAvatar(context, session, vContactAvatar, groupUser.avatarUrl, groupUser.userId, groupUser.getDisplayname());
    }
}
