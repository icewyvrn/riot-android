/*
 * Copyright 2018-2025 New Vector Ltd.
 * Copyright 2017 Vector Creations Ltd
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */

package im.vector.adapters;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.rest.model.group.Group;

import butterknife.BindView;
import im.vector.R;

public class GroupInvitationViewHolder extends GroupViewHolder {

    @BindView(R.id.group_invite_reject_button)
    Button vRejectButton;

    @BindView(R.id.group_invite_join_button)
    Button vJoinButton;

    GroupInvitationViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void populateViews(final Context context,
                              final MXSession session,
                              final Group group,
                              final AbsAdapter.GroupInvitationListener invitationListener,
                              final boolean isInvitation,
                              final AbsAdapter.MoreGroupActionListener moreGroupActionListener) {
        super.populateViews(context, session, group, invitationListener, true, moreGroupActionListener);

        vJoinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != invitationListener) {
                    invitationListener.onJoinGroup(session, group.getGroupId());
                }
            }
        });

        vRejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != invitationListener) {
                    invitationListener.onRejectInvitation(session, group.getGroupId());
                }
            }
        });
    }
}