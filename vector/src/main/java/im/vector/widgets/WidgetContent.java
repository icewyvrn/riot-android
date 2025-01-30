/*
 * Copyright 2025 New Vector Ltd.
 * Copyright 2017 Vector Creations Ltd
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */

package im.vector.widgets;

import android.text.TextUtils;

import com.google.gson.JsonElement;

import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.Log;

import java.io.Serializable;
import java.util.Map;

public class WidgetContent implements Serializable {
    private static final String LOG_TAG = "WidgetContent";

    // widget URL
    public String url;

    // wiget type
    public String type;

    // widget data
    public Map<String, Object> data;

    // widget "human name"
    public String name;

    // widget id
    public String id;

    // creator id
    public String creatorUserId;

    /**
     * @return the human name
     */
    public String getHumanName() {
        if (!TextUtils.isEmpty(name)) {
            return name + " widget";
        } else if (!TextUtils.isEmpty(type)) {
            if (type.contains("widget")) {
                return type;
            } else if (null != id) {
                return type + " " + id;
            } else {
                return type + " widget";
            }
        } else {
            return "Widget " + id;
        }
    }

    /**
     * Convert a json object into a WidgetContent instance
     *
     * @param jsonObject
     * @return
     */
    public static WidgetContent toWidgetContent(JsonElement jsonObject) {
        try {
            return JsonUtils.getGson(false).fromJson(jsonObject, WidgetContent.class);
        } catch (Exception e) {
            Log.e(LOG_TAG, "## toWidgetContent() : failed " + e.getMessage(), e);
        }

        return new WidgetContent();
    }
}

