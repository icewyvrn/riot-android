/*
 * Copyright 2025 New Vector Ltd.
 * Copyright 2017 Vector Creations Ltd
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package im.vector.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.CallSuper;

import org.matrix.androidsdk.MXSession;

import im.vector.Matrix;
import im.vector.activity.VectorGroupDetailsActivity;

public abstract class GroupDetailsBaseFragment extends VectorBaseFragment {
    private static final String LOG_TAG = GroupDetailsBaseFragment.class.getSimpleName();

    private static final String CURRENT_FILTER = "CURRENT_FILTER";

    protected MXSession mSession;
    protected VectorGroupDetailsActivity mActivity;

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSession = Matrix.getInstance(getContext()).getDefaultSession();
        mActivity = (VectorGroupDetailsActivity) getActivity();

        initViews();
    }

    @Override
    @CallSuper
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (null != mActivity) {
            // dismiss the keyboard when swiping
            final View view = mActivity.getCurrentFocus();
            if (view != null) {
                final InputMethodManager inputMethodManager = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }
    /*
     * *********************************************************************************************
     * Abstract methods
     * *********************************************************************************************
     */

    protected abstract void initViews();

    public abstract void refreshViews();
}
