/*
 * Copyright 2018-2025 New Vector Ltd.
 * Copyright 2017 Vector Creations Ltd
 * Copyright 2016 OpenMarket Ltd
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package im.vector.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Filter;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.matrix.androidsdk.core.callback.SuccessCallback;
import org.matrix.androidsdk.rest.model.group.GroupRoom;

import butterknife.BindView;
import im.vector.R;
import im.vector.adapters.GroupDetailsRoomsAdapter;
import im.vector.util.GroupUtils;
import im.vector.view.EmptyViewItemDecoration;
import im.vector.view.SimpleDividerItemDecoration;

public class GroupDetailsRoomsFragment extends GroupDetailsBaseFragment {
    @BindView(R.id.group_recyclerview)
    RecyclerView mRecycler;

    @BindView(R.id.group_search_view)
    SearchView mSearchView;

    private GroupDetailsRoomsAdapter mAdapter;
    private String mCurrentFilter;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_group_details_rooms;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshViews();
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCurrentFilter = mSearchView.getQuery().toString();
        mAdapter.onFilterDone(mCurrentFilter);
    }

    /*
     * *********************************************************************************************
     * UI management
     * *********************************************************************************************
     */

    /**
     * Prepare views
     */
    @Override
    protected void initViews() {
        int margin = (int) getResources().getDimension(R.dimen.item_decoration_left_margin);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        mRecycler.addItemDecoration(new SimpleDividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL, margin));
        mRecycler.addItemDecoration(new EmptyViewItemDecoration(getActivity(), DividerItemDecoration.VERTICAL, 40, 16, 14));
        mAdapter = new GroupDetailsRoomsAdapter(getActivity(), new GroupDetailsRoomsAdapter.OnSelectRoomListener() {
            @Override
            public void onSelectItem(GroupRoom groupRoom, int position) {
                mActivity.showWaitingView();
                GroupUtils.openGroupRoom(mActivity, mSession, groupRoom, new SuccessCallback<Void>() {
                    @Override
                    public void onSuccess(Void info) {
                        mActivity.hideWaitingView();
                    }
                });
            }
        });
        mRecycler.setAdapter(mAdapter);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                if (!TextUtils.equals(mCurrentFilter, newText)) {
                    mAdapter.getFilter().filter(newText, new Filter.FilterListener() {
                        @Override
                        public void onFilterComplete(int count) {
                            mCurrentFilter = newText;
                        }
                    });
                }
                return true;
            }
        });
        mSearchView.setMaxWidth(Integer.MAX_VALUE);
        mSearchView.setQueryHint(getString(R.string.filter_group_rooms));
        mSearchView.setFocusable(false);
        mSearchView.setIconifiedByDefault(false);
        mSearchView.clearFocus();
    }

    @Override
    public void refreshViews() {
        if (!isAdded()) {
            return;
        }

        mAdapter.setGroupRooms(mActivity.getGroup().getGroupRooms().getRoomsList());
    }
}
