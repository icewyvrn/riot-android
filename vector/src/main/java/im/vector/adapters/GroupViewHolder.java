/*
 * Copyright 2018-2025 New Vector Ltd.
 * Copyright 2017 Vector Creations Ltd
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */

package im.vector.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.rest.model.group.Group;

import butterknife.BindView;
import butterknife.ButterKnife;
import im.vector.R;
import im.vector.util.VectorUtils;
import im.vector.util.ViewUtilKt;

public class GroupViewHolder extends RecyclerView.ViewHolder {
    private static final String LOG_TAG = GroupViewHolder.class.getSimpleName();

    @BindView(R.id.group_avatar)
    ImageView vGroupAvatar;

    @BindView(R.id.group_name)
    TextView vGroupName;

    @BindView(R.id.group_topic)
    @Nullable
    TextView vGroupTopic;

    @BindView(R.id.group_members_count)
    TextView vGroupMembersCount;

    @BindView(R.id.group_more_action_click_area)
    @Nullable
    View vGroupMoreActionClickArea;

    @BindView(R.id.group_more_action_anchor)
    @Nullable
    View vGroupMoreActionAnchor;

    public GroupViewHolder(final View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    /**
     * Refresh the holder layout
     *
     * @param context                 the context
     * @param group                   the group
     * @param isInvitation            true if it is an invitation
     * @param moreGroupActionListener the more actions listener
     */
    public void populateViews(final Context context,
                              final MXSession session,
                              final Group group,
                              final AbsAdapter.GroupInvitationListener invitationListener,
                              final boolean isInvitation,
                              final AbsAdapter.MoreGroupActionListener moreGroupActionListener) {
        // sanity check
        if (null == group) {
            Log.e(LOG_TAG, "## populateViews() : null group");
            return;
        }

        if (isInvitation) {
            vGroupMembersCount.setText("!");
            vGroupMembersCount.setTypeface(null, Typeface.BOLD);
            ViewUtilKt.setRoundBackground(vGroupMembersCount, ContextCompat.getColor(context, R.color.vector_fuchsia_color));
            vGroupMembersCount.setVisibility(View.VISIBLE);
        } else {
            vGroupMembersCount.setVisibility(View.GONE);
        }

        vGroupName.setText(group.getDisplayName());
        vGroupName.setTypeface(null, Typeface.NORMAL);

        VectorUtils.loadGroupAvatar(context, session, vGroupAvatar, group);

        vGroupTopic.setText(group.getShortDescription());

        if (vGroupMoreActionClickArea != null && vGroupMoreActionAnchor != null) {
            vGroupMoreActionClickArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != moreGroupActionListener) {
                        moreGroupActionListener.onMoreActionClick(vGroupMoreActionAnchor, group);
                    }
                }
            });
        }
    }
}
