/*
 * Copyright 2025 New Vector Ltd.
 * Copyright 2017 Vector Creations Ltd
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package im.vector.adapters;

import android.content.Context;

import java.util.Comparator;
import java.util.List;

public class GroupAdapterSection<T> extends AdapterSection<T> {

    public GroupAdapterSection(Context context,
                               String title,
                               int headerSubViewResId,
                               int contentResId,
                               int headerViewType,
                               int contentViewType,
                               List<T> items,
                               Comparator<T> comparator) {
        super(context, title, headerSubViewResId, contentResId, headerViewType, contentViewType, items, comparator);
    }

    /**
     * Update the title depending on the number of items
     */
    void updateTitle() {
        String newTitle;

        // the group members / rooms lists are estimated
        // it seems safer to display the count only for the filtered lists
        if ((getItems().size() != getFilteredItems().size()) && (getNbItems() > 0)) {
            newTitle = mTitle.concat("   " + getNbItems());
        } else {
            newTitle = mTitle;
        }

        formatTitle(newTitle);
    }
}
