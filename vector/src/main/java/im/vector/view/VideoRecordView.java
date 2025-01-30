/*
 * Copyright 2025 New Vector Ltd.
 * Copyright 2014 OpenMarket Ltd
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */
package im.vector.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * View that displays a disc representing a percentage.
 */
public class VideoRecordView extends RelativeLayout {

    private VideoRecordProgressView mVideoRecordProgressView;

    /**
     * constructors
     **/
    public VideoRecordView(Context context) {
        super(context);
        initView();
    }

    public VideoRecordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public VideoRecordView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    /**
     * Common initialisation method.
     */
    private void initView() {
        View.inflate(getContext(), im.vector.R.layout.video_record_view, this);

        // retrieve the UI items
        mVideoRecordProgressView = findViewById(im.vector.R.id.video_record_progress_view);
    }

    /**
     * Start the video recording animation
     */
    public void startAnimation() {
        mVideoRecordProgressView.startAnimation();
    }

    /**
     * Stop the animation
     */
    private void stopAnimation() {
        mVideoRecordProgressView.stopAnimation();
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);

        // if the view is hidden
        if ((View.GONE == visibility) || (View.INVISIBLE == visibility)) {
            stopAnimation();
        }
    }
}
