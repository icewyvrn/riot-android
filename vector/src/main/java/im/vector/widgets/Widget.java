/*
 * Copyright 2025 New Vector Ltd.
 * Copyright 2017 Vector Creations Ltd
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package im.vector.widgets;

import android.text.TextUtils;

import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.rest.model.Event;

import java.io.Serializable;
import java.net.URLEncoder;

public class Widget implements Serializable {
    private String mWidgetId;
    private Event mWidgetEvent;
    private String mSessionId;

    private WidgetContent mWidgetContent;
    private String mUrl;

    /**
     * Constructor
     *
     * @param session     the session
     * @param widgetEvent the widget event
     */
    public Widget(MXSession session, Event widgetEvent) throws Exception {
        if (!TextUtils.equals(widgetEvent.type, WidgetsManager.WIDGET_EVENT_TYPE)) {
            throw new Exception("unsupported event type " + widgetEvent.type);
        }

        mWidgetId = widgetEvent.stateKey;
        mWidgetEvent = widgetEvent;
        mSessionId = session.getMyUserId();

        mWidgetContent = WidgetContent.toWidgetContent(widgetEvent.getContentAsJsonObject());
        mUrl = mWidgetContent.url;

        if (null != mUrl) {
            // Format the url string with user data
            mUrl = mUrl.replace("$matrix_user_id", session.getMyUserId());

            String displayName = session.getMyUser().displayname;
            mUrl = mUrl.replace("$matrix_display_name", (null != displayName) ? displayName : session.getMyUserId());

            String avatarUrl = session.getMyUser().getAvatarUrl();
            mUrl = mUrl.replace("$matrix_avatar_url", (null != avatarUrl) ? avatarUrl : "");
        }

        if (null != mWidgetContent.data) {
            for (String key : mWidgetContent.data.keySet()) {
                Object valueAsVoid = mWidgetContent.data.get(key);

                if (valueAsVoid instanceof String) {
                    mUrl = mUrl.replace("$" + key, URLEncoder.encode((String) valueAsVoid, "utf-8"));
                }
            }
        }
    }

    /**
     * Tells if the widget is active
     *
     * @return true if the widget is active
     */
    public boolean isActive() {
        return (null != mWidgetContent.type) && (null != mUrl);
    }

    /**
     * Getters
     */
    public String getWidgetId() {
        return mWidgetId;
    }

    public Event getWidgetEvent() {
        return mWidgetEvent;
    }

    public String getSessionId() {
        return mSessionId;
    }

    public String getRoomId() {
        return mWidgetEvent.roomId;
    }

    private String getType() {
        return mWidgetContent.type;
    }

    public String getUrl() {
        return mUrl;
    }

    private String getName() {
        return mWidgetContent.name;
    }

    public String getHumanName() {
        return mWidgetContent.getHumanName();
    }

    @Override
    public String toString() {
        return "Widget id: " + getWidgetId() + " - type: " + getType() + " - name: " + getName() + " - url: " + getUrl();
    }
}
