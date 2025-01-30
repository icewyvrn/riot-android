/*
 * Copyright 2018-2025 New Vector Ltd.
 * Copyright 2017 Vector Creations Ltd
 * Copyright 2014 OpenMarket Ltd
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package im.vector.activity;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import butterknife.BindView;
import im.vector.R;

/**
 * LoggingOutActivity displays an animation while a session log out is in progress.
 */
public class LoggingOutActivity extends MXCActionBarActivity {

    @BindView(R.id.animated_logo_image_view)
    ImageView animatedLogo;

    @Override
    public int getLayoutRes() {
        return R.layout.vector_activity_splash;
    }

    @Override
    public void initUiAndData() {
        Drawable background = animatedLogo.getBackground();
        if (background instanceof AnimationDrawable) {
            ((AnimationDrawable) background).start();
        }
    }
}
