/*
 * Copyright 2025 New Vector Ltd.
 * Copyright 2015 OpenMarket Ltd
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package im.vector.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import im.vector.R;

/**
 * An adapter which can display string
 */
public class ImageSizesAdapter extends ArrayAdapter<ImageCompressionDescription> {
    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private final int mLayoutResourceId;

    /**
     * Construct an adapter which will display a list of image size
     *
     * @param context          Activity context
     * @param layoutResourceId The resource ID of the layout for each item.
     */
    public ImageSizesAdapter(Context context, int layoutResourceId) {
        super(context, layoutResourceId);
        mContext = context;
        mLayoutResourceId = layoutResourceId;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(mLayoutResourceId, parent, false);
        }

        ImageCompressionDescription imageSizesDescription = getItem(position);

        TextView textView = convertView.findViewById(R.id.ImageSizesAdapter_format);
        textView.setText(imageSizesDescription.mCompressionText);

        textView = convertView.findViewById(R.id.ImageSizesAdapter_info);
        textView.setText(imageSizesDescription.mCompressionInfoText);
        return convertView;
    }
}
