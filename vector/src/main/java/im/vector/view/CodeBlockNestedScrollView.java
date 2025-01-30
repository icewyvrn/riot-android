/*
 * Copyright 2025 New Vector Ltd.
 * Copyright 2016 OpenMarket Ltd
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package im.vector.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.core.widget.NestedScrollView;

public class CodeBlockNestedScrollView extends NestedScrollView {

    public CodeBlockNestedScrollView(Context context) {
        super(context);
    }

    public CodeBlockNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CodeBlockNestedScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(500, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}