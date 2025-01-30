/*
 * Copyright 2025 New Vector Ltd.
 * Copyright 2014 OpenMarket Ltd
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */
package im.vector.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;

import java.lang.ref.WeakReference;

/**
 * Avatar image view used in PillView
 */
public class PillImageView extends VectorCircularImageView {
    // listener
    private WeakReference<PillView.OnUpdateListener> mOnUpdateListener = null;

    public PillImageView(Context context) {
        super(context);
    }

    public PillImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PillImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void setCircularImageDrawable(final RoundedBitmapDrawable cachedDrawable) {
        super.setCircularImageDrawable(cachedDrawable);

        if ((null != mOnUpdateListener) && (null != mOnUpdateListener.get())) {
            mOnUpdateListener.get().onAvatarUpdate();
        }
    }

    /**
     * Update the update listener
     *
     * @param listener the new update listener
     */
    public void setOnUpdateListener(PillView.OnUpdateListener listener) {
        mOnUpdateListener = new WeakReference<>(listener);
    }
}
