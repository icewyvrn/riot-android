/*
 * Copyright 2018-2025 New Vector Ltd.
 * Copyright 2014 OpenMarket Ltd
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package im.vector.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

import org.matrix.androidsdk.core.Log;

/**
 * Patch the issue "https://code.google.com/p/android/issues/detail?id=66620"
 * and https://issuetracker.google.com/issues/36931456
 */
public class VectorViewPager extends ViewPager {
    private static final String LOG_TAG = VectorViewPager.class.getSimpleName();

    public VectorViewPager(Context context) {
        super(context);
    }

    public VectorViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (getAdapter() == null || getAdapter().getCount() == 0) {
            return false;
        }

        try {
            return super.onInterceptTouchEvent(event);
        } catch (IllegalArgumentException exception) {
            Log.e(LOG_TAG, "Exception: " + exception.getLocalizedMessage());
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (getAdapter() == null || getAdapter().getCount() == 0) {
            return false;
        }

        try {
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException exception) {
            Log.e(LOG_TAG, "Exception: " + exception.getLocalizedMessage());
        }
        return false;
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        try {
            return super.getChildDrawingOrder(childCount, i);
        } catch (Exception e) {
            Log.e(LOG_TAG, "## getChildDrawingOrder() failed " + e.getMessage(), e);
        }

        return 0;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        try {
            super.dispatchDraw(canvas);
        } catch (Exception e) {
            Log.e(LOG_TAG, "## dispatchDraw() failed " + e.getMessage(), e);
        }
    }
}
