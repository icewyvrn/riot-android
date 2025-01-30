/*
 * Copyright 2018-2025 New Vector Ltd.
 * Copyright 2015 OpenMarket Ltd
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package im.vector.fragments;

import android.text.TextUtils;
import android.widget.Toast;

import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.fragments.MatrixMessagesFragment;

import im.vector.Matrix;
import im.vector.R;

public class VectorMessagesFragment extends MatrixMessagesFragment {
    private static final String LOG_TAG = VectorMessagesFragment.class.getSimpleName();

    public static VectorMessagesFragment newInstance(String roomId) {
        VectorMessagesFragment fragment = new VectorMessagesFragment();
        fragment.setArguments(getArgument(roomId));
        return fragment;
    }

    @Override
    protected void displayInitializeTimelineError(Object error) {
        String errorMessage = "";

        if (error instanceof MatrixError) {
            MatrixError matrixError = (MatrixError) error;

            if (TextUtils.equals(matrixError.errcode, MatrixError.NOT_FOUND)) {
                errorMessage = getContext().getString(R.string.failed_to_load_timeline_position, Matrix.getApplicationName());
            } else {
                errorMessage = matrixError.getLocalizedMessage();
            }
        } else if (error instanceof Exception) {
            errorMessage = ((Exception) error).getLocalizedMessage();
        }

        if (!TextUtils.isEmpty(errorMessage)) {
            Log.d(LOG_TAG, "displayInitializeTimelineError : " + errorMessage);
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
        }
    }
}
