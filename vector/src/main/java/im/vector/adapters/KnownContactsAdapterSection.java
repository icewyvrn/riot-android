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

import java.util.Comparator;
import java.util.List;

class KnownContactsAdapterSection extends AdapterSection<ParticipantAdapterItem> {

    // Tells if the search result is limited
    private boolean mIsLimited;
    private String mCustomHeaderExtra;

    public KnownContactsAdapterSection(Context context, String title, int headerSubViewResId, int contentResId, int headerViewType,
                                       int contentViewType, List<ParticipantAdapterItem> items, Comparator<ParticipantAdapterItem> comparator) {
        super(context, title, headerSubViewResId, contentResId, headerViewType, contentViewType, items, comparator);
    }

    /**
     * Tells that the search result is limited
     *
     * @param isLimited true if limited
     */
    public void setIsLimited(boolean isLimited) {
        mIsLimited = isLimited;
    }

    /**
     * Defines a custom extra string
     *
     * @param extraHeader the extra header string
     */
    public void setCustomHeaderExtra(String extraHeader) {
        mCustomHeaderExtra = extraHeader;
    }

    // FIXME i18n
    @Override
    protected void updateTitle() {
        String newTitle;

        if (getNbItems() > 0) {
            if (!TextUtils.isEmpty(mCustomHeaderExtra)) {
                newTitle = mTitle.concat("   " + mCustomHeaderExtra + ", " + getNbItems());
            } else if (!mIsLimited) {
                newTitle = mTitle.concat("   " + getNbItems());
            } else {
                newTitle = mTitle.concat("   >" + getNbItems());
            }
        } else {
            newTitle = mTitle;
        }

        formatTitle(newTitle);
    }
}
