/*
 * Copyright 2018-2025 New Vector Ltd.
 * Copyright 2017 Vector Creations Ltd
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package im.vector.adapters;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.data.Room;

import butterknife.BindView;
import im.vector.R;

public class RoomInvitationViewHolder extends RoomViewHolder {

    @BindView(R.id.room_invite_reject_button)
    Button vRejectButton;

    @BindView(R.id.room_invite_preview_button)
    Button vPreViewButton;

    RoomInvitationViewHolder(View itemView) {
        super(itemView);
    }

    void populateViews(final Context context,
                       final MXSession session,
                       final Room room,
                       final AbsAdapter.RoomInvitationListener invitationListener,
                       final AbsAdapter.MoreRoomActionListener moreRoomActionListener) {
        super.populateViews(context, session, room, room.isDirectChatInvitation(), true, moreRoomActionListener);

        vPreViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != invitationListener) {
                    invitationListener.onPreviewRoom(session, room.getRoomId());
                }
            }
        });

        vRejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != invitationListener) {
                    invitationListener.onRejectInvitation(session, room.getRoomId());
                }
            }
        });
    }
}