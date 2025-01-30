/*
 * Copyright 2018-2025 New Vector Ltd.
 * Copyright 2017 Vector Creations Ltd
 * Copyright 2016 OpenMarket Ltd
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package im.vector.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;

import org.matrix.androidsdk.adapters.MessageRow;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.message.FileMessage;
import org.matrix.androidsdk.rest.model.message.Message;

import java.util.ArrayList;
import java.util.List;

import im.vector.activity.VectorMediaViewerActivity;
import im.vector.adapters.VectorMessagesAdapter;
import im.vector.adapters.VectorSearchFilesListAdapter;
import im.vector.util.SlidableMediaInfo;

public class VectorSearchRoomsFilesListFragment extends VectorSearchMessagesListFragment {
    /**
     * static constructor
     *
     * @param matrixId    the session Id.
     * @param layoutResId the used layout.
     * @return the instance
     */
    public static VectorSearchRoomsFilesListFragment newInstance(String matrixId, String roomId, int layoutResId) {
        VectorSearchRoomsFilesListFragment frag = new VectorSearchRoomsFilesListFragment();
        frag.setArguments(getArguments(matrixId, roomId, layoutResId));
        return frag;
    }

    @Override
    public VectorMessagesAdapter createMessagesAdapter() {
        mIsMediaSearch = true;
        return new VectorSearchFilesListAdapter(mSession, getActivity(), (null == mRoomId), getMXMediaCache());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        mMessageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MessageRow row = mAdapter.getItem(position);
                Event event = row.getEvent();

                if (mAdapter.isInSelectionMode()) {
                    // cancel the selection mode.
                    mAdapter.onEventTap(null);
                    return;
                }

                Message message = JsonUtils.toMessage(event.getContent());

                // video and images are displayed inside a medias slider.
                if (Message.MSGTYPE_IMAGE.equals(message.msgtype) || (Message.MSGTYPE_VIDEO.equals(message.msgtype))) {
                    List<SlidableMediaInfo> mediaMessagesList = listSlidableMessages();
                    int listPosition = getMediaMessagePosition(mediaMessagesList, message);

                    if (listPosition >= 0) {
                        Intent viewImageIntent = new Intent(getActivity(), VectorMediaViewerActivity.class);

                        viewImageIntent.putExtra(VectorMediaViewerActivity.EXTRA_MATRIX_ID, mSession.getCredentials().userId);
                        viewImageIntent.putExtra(VectorMediaViewerActivity.KEY_THUMBNAIL_WIDTH, mAdapter.getMaxThumbnailWidth());
                        viewImageIntent.putExtra(VectorMediaViewerActivity.KEY_THUMBNAIL_HEIGHT, mAdapter.getMaxThumbnailHeight());
                        viewImageIntent.putExtra(VectorMediaViewerActivity.KEY_INFO_LIST, (ArrayList) mediaMessagesList);
                        viewImageIntent.putExtra(VectorMediaViewerActivity.KEY_INFO_LIST_INDEX, listPosition);

                        getActivity().startActivity(viewImageIntent);
                    }
                } else if (Message.MSGTYPE_FILE.equals(message.msgtype)) {
                    FileMessage fileMessage = JsonUtils.toFileMessage(event.getContent());

                    if (null != fileMessage.getUrl()) {
                        onMediaAction(ACTION_VECTOR_OPEN, fileMessage.getUrl(), fileMessage.getMimeType(), fileMessage.body, fileMessage.file);
                    }
                }
            }
        });

        return view;
    }
}
