/*
 * Copyright 2017 Vector Creations Ltd
 * Copyright 2018 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package im.vector.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.data.RoomPreviewData;
import org.matrix.androidsdk.rest.client.EventsRestClient;
import org.matrix.androidsdk.rest.model.publicroom.PublicRoom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import im.vector.PublicRoomsManager;
import im.vector.R;
import im.vector.activity.CommonActivityUtils;
import im.vector.activity.RoomDirectoryPickerActivity;
import im.vector.activity.VectorRoomActivity;
import im.vector.adapters.RoomAdapter;
import im.vector.adapters.RoomsListAdapter;
import im.vector.ui.themes.ThemeUtils;
import im.vector.util.HomeRoomsViewModel;
import im.vector.util.RoomDirectoryData;

public class RoomsFragment extends AbsHomeFragment implements AbsHomeFragment.OnRoomChangedListener {
    private static final String LOG_TAG = RoomsFragment.class.getSimpleName();

    // activity result codes
    private static final int DIRECTORY_SOURCE_ACTIVITY_REQUEST_CODE = 314;

    //
    private static final String SELECTED_ROOM_DIRECTORY = "SELECTED_ROOM_DIRECTORY";

    // estimated number of public rooms
    private Integer mEstimatedPublicRoomCount = null;

    @BindView(R.id.listview)
    ListView mListView;

    // rooms management
    private RoomsListAdapter mAdapter;

    // the selected room directory
    private RoomDirectoryData mSelectedRoomDirectory;

    // rooms list
    private List<Room> mRooms = new ArrayList<>();
    private boolean mHasInitializedPublicRooms;

    private int mLastVisibleItem = -1;
    private final Runnable mInitialPublicRoomsRunnable = new Runnable() {
        @Override
        public void run() {
            if (isAdded() && !mHasInitializedPublicRooms) {
                initPublicRooms(false);
            }
        }
    };

    private final AbsListView.OnScrollListener mListScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            // no-op
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if ((mActivity != null) && visibleItemCount > 0 && totalItemCount > visibleItemCount && firstVisibleItem != mLastVisibleItem) {
                mActivity.hideFloatingActionButton(getTag());
            }

            mLastVisibleItem = firstVisibleItem;

            if (mAdapter.getPublicRoomCount() > 0 && (firstVisibleItem + visibleItemCount + 10) >= totalItemCount) {
                forwardPaginate();
            }
        }
    };

    /*
     * *********************************************************************************************
     * Static methods
     * *********************************************************************************************
     */

    public static RoomsFragment newInstance() {
        return new RoomsFragment();
    }

    /*
     * *********************************************************************************************
     * Fragment lifecycle
     * *********************************************************************************************
     */

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_rooms;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mPrimaryColor = ThemeUtils.INSTANCE.getColor(getActivity(), R.attr.vctr_tab_home);
        mSecondaryColor = ThemeUtils.INSTANCE.getColor(getActivity(), R.attr.vctr_tab_home_secondary);

        mFabColor = ContextCompat.getColor(getActivity(), R.color.tab_rooms);
        mFabPressedColor = ContextCompat.getColor(getActivity(), R.color.tab_rooms_secondary);

        initViews();

        mOnRoomChangedListener = this;

        if (!TextUtils.isEmpty(mCurrentFilter)) {
            mAdapter.getFilter().filter(mCurrentFilter);
        }

        if (savedInstanceState != null) {
            mSelectedRoomDirectory = (RoomDirectoryData) savedInstanceState.getSerializable(SELECTED_ROOM_DIRECTORY);
        }

        refreshDirectorySourceSpinner();
        scheduleInitialPublicRoomsInit();
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.setInvitation(mActivity.getRoomInvitations());
        mListView.setOnScrollListener(mListScrollListener);
        focusFirstRoomRow();
    }

    @Override
    public void onPause() {
        super.onPause();
        mEstimatedPublicRoomCount = null;
        mListView.setOnScrollListener(null);
        mListView.removeCallbacks(mInitialPublicRoomsRunnable);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save the selected room directory
        outState.putSerializable(SELECTED_ROOM_DIRECTORY, mSelectedRoomDirectory);
    }

    /*
     * *********************************************************************************************
     * Abstract methods implementation
     * *********************************************************************************************
     */

    @Override
    protected List<Room> getRooms() {
        return new ArrayList<>(mRooms);
    }

    @Override
    protected void onFilter(String pattern, final OnFilterListener listener) {
        mAdapter.getFilter().filter(pattern, new Filter.FilterListener() {
            @Override
            public void onFilterComplete(int count) {
                Log.i(LOG_TAG, "onFilterComplete " + count);
                if (listener != null) {
                    listener.onFilterDone(count);
                }

                // trigger the public rooms search to avoid unexpected list refresh
                mHasInitializedPublicRooms = true;
                initPublicRooms(false);
            }
        });
    }

    @Override
    protected void onResetFilter() {
        mAdapter.getFilter().filter("", new Filter.FilterListener() {
            @Override
            public void onFilterComplete(int count) {
                Log.i(LOG_TAG, "onResetFilter " + count);

                // trigger the public rooms search to avoid unexpected list refresh
                mHasInitializedPublicRooms = true;
                initPublicRooms(false);
            }
        });
    }

    /*
     * *********************************************************************************************
     * Public methods
     * *********************************************************************************************
     */

    @Override
    public void onRoomResultUpdated(final HomeRoomsViewModel.Result result) {
        if (isResumed()) {
            mRooms = result.getOtherRoomsWithFavorites();
            mAdapter.setRooms(mRooms);
            mAdapter.setInvitation(mActivity.getRoomInvitations());
            focusFirstRoomRow();
        }
    }

    /*
     * *********************************************************************************************
     * UI management
     * *********************************************************************************************
     */

    private void initViews() {
        mListView.setDivider(null);
        mListView.setDividerHeight(0);
        mListView.setItemsCanFocus(false);
        mListView.setScrollingCacheEnabled(false);
        mListView.setFocusable(true);
        mListView.setFocusableInTouchMode(true);
        mListView.setSelector(R.drawable.bb_holo_list_selector);

        mAdapter = new RoomsListAdapter(getActivity(), mSession, new RoomAdapter.OnSelectItemListener() {
            @Override
            public void onSelectItem(Room room, int position) {
                openRoom(room);
            }

            @Override
            public void onSelectItem(PublicRoom publicRoom) {
                onPublicRoomSelected(publicRoom);
            }
        }, this, this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListView.setItemChecked(position, true);

                Object item = mAdapter.getItem(position);
                if (item instanceof Room) {
                    Room room = (Room) item;
                    if (room.isInvited()) {
                        onPreviewRoom(mSession, room.getRoomId());
                    } else {
                        openRoom(room);
                    }
                } else if (item instanceof PublicRoom) {
                    onPublicRoomSelected((PublicRoom) item);
                }
            }
        });
    }

    private void scheduleInitialPublicRoomsInit() {
        mListView.removeCallbacks(mInitialPublicRoomsRunnable);
        mListView.postDelayed(mInitialPublicRoomsRunnable, 1000L);
    }

    private void focusFirstRoomRow() {
        if (mListView == null || mAdapter == null || mAdapter.getCount() == 0) {
            return;
        }

        final int firstSelectablePosition = mAdapter.getFirstSelectablePosition();
        if (firstSelectablePosition == ListView.INVALID_POSITION) {
            return;
        }

        mListView.post(new Runnable() {
            @Override
            public void run() {
                mListView.requestFocus();
                mListView.setSelection(firstSelectablePosition);
                mListView.setItemChecked(firstSelectablePosition, true);
            }
        });
    }

    /*
     * *********************************************************************************************
     * Public rooms management
     * *********************************************************************************************
     */

    // spinner text
    private ArrayAdapter<CharSequence> mRoomDirectoryAdapter;

    /**
     * Handle a public room selection
     *
     * @param publicRoom the public room
     */
    private void onPublicRoomSelected(final PublicRoom publicRoom) {
        // sanity check
        if (null != publicRoom.roomId) {
            final RoomPreviewData roomPreviewData = new RoomPreviewData(mSession, publicRoom.roomId, null, publicRoom.canonicalAlias, null);

            // Check whether the room exists to handled the cases where the user is invited or he has joined.
            // CAUTION: the room may exist whereas the user membership is neither invited nor joined.
            final Room room = mSession.getDataHandler().getRoom(publicRoom.roomId, false);
            if (null != room && room.isInvited()) {
                Log.d(LOG_TAG, "onPublicRoomSelected : the user is invited -> display the preview " + getActivity());
                CommonActivityUtils.previewRoom(getActivity(), roomPreviewData);
            } else if (null != room && room.isJoined()) {
                Log.d(LOG_TAG, "onPublicRoomSelected : the user joined the room -> open the room");
                final Map<String, Object> params = new HashMap<>();
                params.put(VectorRoomActivity.EXTRA_MATRIX_ID, mSession.getMyUserId());
                params.put(VectorRoomActivity.EXTRA_ROOM_ID, publicRoom.roomId);

                if (!TextUtils.isEmpty(publicRoom.name)) {
                    params.put(VectorRoomActivity.EXTRA_DEFAULT_NAME, publicRoom.name);
                }

                if (!TextUtils.isEmpty(publicRoom.topic)) {
                    params.put(VectorRoomActivity.EXTRA_DEFAULT_TOPIC, publicRoom.topic);
                }

                CommonActivityUtils.goToRoomPage(getActivity(), mSession, params);
            } else {
                // Display a preview by default.
                Log.d(LOG_TAG, "onPublicRoomSelected : display the preview");
                mActivity.showWaitingView();

                roomPreviewData.fetchPreviewData(new ApiCallback<Void>() {
                    private void onDone() {
                        if (null != mActivity) {
                            mActivity.hideWaitingView();
                            CommonActivityUtils.previewRoom(getActivity(), roomPreviewData);
                        }
                    }

                    @Override
                    public void onSuccess(Void info) {
                        onDone();
                    }

                    private void onError() {
                        roomPreviewData.setPublicRoom(publicRoom);
                        roomPreviewData.setRoomName(publicRoom.name);
                        onDone();
                    }

                    @Override
                    public void onNetworkError(Exception e) {
                        onError();
                    }

                    @Override
                    public void onMatrixError(MatrixError e) {
                        onError();
                    }

                    @Override
                    public void onUnexpectedError(Exception e) {
                        onError();
                    }
                });
            }
        }
    }

    /**
     * Refresh the directory source spinner
     */
    private void refreshDirectorySourceSpinner() {
        // no directory source, use the default one
        if (null == mSelectedRoomDirectory) {
            mSelectedRoomDirectory = RoomDirectoryData.getDefault();
        }

        if (null == mRoomDirectoryAdapter) {
            mRoomDirectoryAdapter = new ArrayAdapter<>(getActivity(), R.layout.public_room_spinner_item);
        } else {
            mRoomDirectoryAdapter.clear();
        }

        mRoomDirectoryAdapter.add(mSelectedRoomDirectory.getDisplayName());
        mAdapter.setDirectoryAdapter(mRoomDirectoryAdapter);
        mAdapter.setSelectedDirectoryDisplayName(mSelectedRoomDirectory.getDisplayName());
        mAdapter.setDirectoryTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    startActivityForResult(RoomDirectoryPickerActivity.getIntent(getActivity(), mSession.getMyUserId()),
                            DIRECTORY_SOURCE_ACTIVITY_REQUEST_CODE);
                }
                return true;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK == resultCode) {
            if (requestCode == DIRECTORY_SOURCE_ACTIVITY_REQUEST_CODE) {
                mSelectedRoomDirectory = (RoomDirectoryData) data.getSerializableExtra(RoomDirectoryPickerActivity.EXTRA_OUT_ROOM_DIRECTORY_DATA);
                mAdapter.setPublicRooms(new ArrayList<PublicRoom>());
                mHasInitializedPublicRooms = true;
                initPublicRooms(true);
            }
        }
    }

    /**
     * Display the public rooms loading view
     */
    private void showPublicRoomsLoadingView() {
        // No dedicated loading row for the ListView rewrite.
    }

    /**
     * Hide the public rooms loading view
     */
    private void hidePublicRoomsLoadingView() {
        // No dedicated loading row for the ListView rewrite.
    }

    /**
     * Init the public rooms.
     *
     * @param displayOnTop true to display the public rooms in full screen
     */
    private void initPublicRooms(final boolean displayOnTop) {
        mHasInitializedPublicRooms = true;
        refreshDirectorySourceSpinner();

        showPublicRoomsLoadingView();

        mAdapter.setNoMorePublicRooms(false);

        if (null == mEstimatedPublicRoomCount) {
            final EventsRestClient eventsRestClient = mSession != null ? mSession.getEventsApiClient() : null;
            if (eventsRestClient == null) {
                hidePublicRoomsLoadingView();
                return;
            }
            eventsRestClient.getPublicRoomsCount(
                    mSelectedRoomDirectory.getHomeServer(),
                    mSelectedRoomDirectory.getThirdPartyInstanceId(),
                    mSelectedRoomDirectory.isIncludedAllNetworks(),
                    new ApiCallback<Integer>() {
                        private void onDone(int count) {
                            mEstimatedPublicRoomCount = count;
                            mAdapter.setEstimatedPublicRoomsCount(count);

                            // next step
                            initPublicRooms(displayOnTop);
                        }

                        @Override
                        public void onSuccess(Integer count) {
                            if (null != count) {
                                onDone(count);
                            } else {
                                onDone(-1);
                            }
                        }

                        @Override
                        public void onNetworkError(Exception e) {
                            Log.e(LOG_TAG, "## startPublicRoomsSearch() : getPublicRoomsCount failed " + e.getMessage(), e);
                            onDone(-1);
                        }

                        @Override
                        public void onMatrixError(MatrixError e) {
                            Log.e(LOG_TAG, "## startPublicRoomsSearch() : getPublicRoomsCount failed " + e.getMessage());
                            onDone(-1);
                        }

                        @Override
                        public void onUnexpectedError(Exception e) {
                            Log.e(LOG_TAG, "## startPublicRoomsSearch() : getPublicRoomsCount failed " + e.getMessage(), e);
                            onDone(-1);
                        }
                    }
            );
            return;
        }

        PublicRoomsManager.getInstance().startPublicRoomsSearch(mSelectedRoomDirectory.getHomeServer(),
                mSelectedRoomDirectory.getThirdPartyInstanceId(),
                mSelectedRoomDirectory.isIncludedAllNetworks(),
                mCurrentFilter, new ApiCallback<List<PublicRoom>>() {
                    @Override
                    public void onSuccess(List<PublicRoom> publicRooms) {
                        if (isAdded()) {
                            mAdapter.setNoMorePublicRooms(publicRooms.size() < PublicRoomsManager.PUBLIC_ROOMS_LIMIT);
                            mAdapter.setPublicRooms(publicRooms);
                            focusFirstRoomRow();

                            if (displayOnTop) {
                                mListView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mListView.setSelection(mAdapter.getPublicRoomsHeaderPosition());
                                    }
                                });
                            }

                            hidePublicRoomsLoadingView();
                        }
                    }

                    private void onError(String message) {
                        if (isAdded()) {
                            Log.e(LOG_TAG, "## startPublicRoomsSearch() failed " + message);
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            hidePublicRoomsLoadingView();
                        }
                    }

                    @Override
                    public void onNetworkError(Exception e) {
                        onError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onMatrixError(MatrixError e) {
                        onError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onUnexpectedError(Exception e) {
                        onError(e.getLocalizedMessage());
                    }
                });
    }

    /**
     * Trigger a forward room pagination
     */
    private void forwardPaginate() {
        if (PublicRoomsManager.getInstance().isRequestInProgress()) {
            return;
        }

        boolean isForwarding = PublicRoomsManager.getInstance().forwardPaginate(new ApiCallback<List<PublicRoom>>() {
            @Override
            public void onSuccess(final List<PublicRoom> publicRooms) {
                if (isAdded()) {
                    if (!PublicRoomsManager.getInstance().hasMoreResults()) {
                        mAdapter.setNoMorePublicRooms(true);
                    }

                    mAdapter.addPublicRooms(publicRooms);
                    hidePublicRoomsLoadingView();
                }
            }

            private void onError(String message) {
                if (isAdded()) {
                    Log.e(LOG_TAG, "## forwardPaginate() failed " + message);
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                    hidePublicRoomsLoadingView();
                }
            }

            @Override
            public void onNetworkError(Exception e) {
                onError(e.getLocalizedMessage());
            }

            @Override
            public void onMatrixError(MatrixError e) {
                onError(e.getLocalizedMessage());
            }

            @Override
            public void onUnexpectedError(Exception e) {
                onError(e.getLocalizedMessage());
            }
        });

        if (isForwarding) {
            showPublicRoomsLoadingView();
        } else {
            hidePublicRoomsLoadingView();
        }
    }

    /*
     * *********************************************************************************************
     * Listeners
     * *********************************************************************************************
     */

    @Override
    public void onToggleDirectChat(String roomId, boolean isDirectChat) {
    }

    @Override
    public void onRoomLeft(String roomId) {
    }

    @Override
    public void onRoomForgot(String roomId) {
        // there is no sync event when a room is forgotten
    }
}
