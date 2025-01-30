/*
 * Copyright 2018-2025 New Vector Ltd.
 * Copyright 2017 Vector Creations Ltd
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package im.vector.adapters;

import android.content.Context;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.recyclerview.widget.RecyclerView;

import org.matrix.androidsdk.MXSession;

import im.vector.Matrix;

/**
 * Abstract adapter to manage filtering
 *
 * @param <T> view holder type
 */
public abstract class AbsFilterableAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> implements Filterable {

    final Context mContext;
    final MXSession mSession;

    CharSequence mCurrentFilterPattern;
    private final Filter mFilter;

    AbsAdapter.RoomInvitationListener mRoomInvitationListener;
    AbsAdapter.GroupInvitationListener mGroupInvitationListener;
    AbsAdapter.MoreRoomActionListener mMoreRoomActionListener;
    AbsAdapter.MoreGroupActionListener mMoreGroupActionListener;
    /*
     * *********************************************************************************************
     * Constructor
     * *********************************************************************************************
     */

    AbsFilterableAdapter(final Context context) {
        mContext = context;

        mSession = Matrix.getInstance(context).getDefaultSession();
        mFilter = createFilter();
    }

    AbsFilterableAdapter(final Context context,
                         final AbsAdapter.RoomInvitationListener invitationListener,
                         final AbsAdapter.MoreRoomActionListener moreActionListener) {
        mContext = context;

        mRoomInvitationListener = invitationListener;
        mMoreRoomActionListener = moreActionListener;

        mSession = Matrix.getInstance(context).getDefaultSession();
        mFilter = createFilter();
    }

    AbsFilterableAdapter(final Context context,
                         final AbsAdapter.GroupInvitationListener invitationListener,
                         final AbsAdapter.MoreGroupActionListener moreActionListener) {
        mContext = context;

        mGroupInvitationListener = invitationListener;
        mMoreGroupActionListener = moreActionListener;

        mSession = Matrix.getInstance(context).getDefaultSession();
        mFilter = createFilter();
    }

    /*
     * *********************************************************************************************
     * Filtering methods
     * *********************************************************************************************
     */

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    public void onFilterDone(CharSequence currentPattern) {
        mCurrentFilterPattern = currentPattern;
    }

    /*
     * *********************************************************************************************
     * Abstract methods
     * *********************************************************************************************
     */

    protected abstract Filter createFilter();

}
