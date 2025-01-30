/*
 * Copyright 2018-2025 New Vector Ltd.
 * Copyright 2016 OpenMarket Ltd
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package im.vector.activity;

import android.content.Intent;

import androidx.fragment.app.FragmentManager;

import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.Log;

import im.vector.R;
import im.vector.fragments.VectorPublicRoomsListFragment;

/**
 * Displays a list of public rooms
 */
public class VectorPublicRoomsActivity extends MXCActionBarActivity {
    private static final String LOG_TAG = VectorPublicRoomsActivity.class.getSimpleName();

    public static final String EXTRA_SEARCHED_PATTERN = "VectorPublicRoomsActivity.EXTRA_SEARCHED_PATTERN";
    private static final String TAG_FRAGMENT_PUBLIC_ROOMS_LIST = "VectorPublicRoomsActivity.TAG_FRAGMENT_PUBLIC_ROOMS_LIST";

    @Override
    public int getLayoutRes() {
        return R.layout.activity_vector_public_rooms;
    }

    @Override
    public int getTitleRes() {
        return R.string.directory_title;
    }

    @Override
    public void initUiAndData() {
        configureToolbar();

        if (CommonActivityUtils.shouldRestartApp(this)) {
            CommonActivityUtils.restartApp(this);
            Log.d(LOG_TAG, "onCreate : restart the application");
            return;
        }

        if (CommonActivityUtils.isGoingToSplash(this)) {
            Log.d(LOG_TAG, "onCreate : Going to splash screen");
            return;
        }

        Intent intent = getIntent();

        MXSession session = getSession(intent);

        FragmentManager fm = getSupportFragmentManager();
        VectorPublicRoomsListFragment vectorPublicRoomsListFragment = (VectorPublicRoomsListFragment) fm.findFragmentByTag(TAG_FRAGMENT_PUBLIC_ROOMS_LIST);

        if (null == vectorPublicRoomsListFragment) {
            String pattern = null;

            if (intent.hasExtra(EXTRA_SEARCHED_PATTERN)) {
                pattern = intent.getStringExtra(EXTRA_SEARCHED_PATTERN);
            }

            vectorPublicRoomsListFragment = VectorPublicRoomsListFragment
                    .newInstance(session.getMyUserId(), R.layout.fragment_vector_public_rooms_list, pattern);
            fm.beginTransaction().add(R.id.layout_public__rooms_list, vectorPublicRoomsListFragment, TAG_FRAGMENT_PUBLIC_ROOMS_LIST).commit();
        }
    }
}


