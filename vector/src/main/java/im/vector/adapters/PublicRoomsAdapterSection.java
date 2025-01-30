/*
 * Copyright 2025 New Vector Ltd.
 * Copyright 2017 Vector Creations Ltd
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package im.vector.adapters;

import android.content.Context;
import android.text.TextUtils;

import org.matrix.androidsdk.rest.model.publicroom.PublicRoom;

import java.util.Comparator;
import java.util.List;

class PublicRoomsAdapterSection extends AdapterSection<PublicRoom> {

    // estimated public rooms count
    // the server should provide this value
    private int mEstimatedPublicRoomsCount = -1;

    // tell if the
    private boolean mHasMoreResults;

    public PublicRoomsAdapterSection(Context context, String title, int headerSubViewResId, int contentResId, int headerViewType,
                                     int contentViewType, List<PublicRoom> items, Comparator<PublicRoom> comparator) {
        super(context, title, headerSubViewResId, contentResId, headerViewType, contentViewType, items, comparator);
    }

    @Override
    protected void updateTitle() {
        String newTitle;
        if (TextUtils.isEmpty(mCurrentFilterPattern)) {
            if (mEstimatedPublicRoomsCount > 0) {
                newTitle = mTitle.concat("   " + mEstimatedPublicRoomsCount);
            } else {
                newTitle = mTitle;
            }
        } else if (getNbItems() > 0) {
            if (mHasMoreResults) {
                newTitle = mTitle.concat("   " + getNbItems());
            } else {
                newTitle = mTitle.concat("   >" + getNbItems());
            }
        } else {
            newTitle = mTitle;
        }

        formatTitle(newTitle);
    }

    /**
     * Update the extimated rooms count.
     *
     * @param estimatedValue the estimated count
     */
    public void setEstimatedPublicRoomsCount(int estimatedValue) {
        mEstimatedPublicRoomsCount = estimatedValue;
        mHasMoreResults = false;
    }

    /**
     * Tells there is no more value to retrieve
     */
    public void setHasMoreResults(boolean noMore) {
        mHasMoreResults = noMore;
    }
}
