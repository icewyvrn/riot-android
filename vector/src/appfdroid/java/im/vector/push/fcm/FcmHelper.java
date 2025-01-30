/*
 * Copyright 2018-2025 New Vector Ltd.
 * Copyright 2014 OpenMarket Ltd
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */
package im.vector.push.fcm;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FcmHelper {

    /**
     * Retrieves the FCM registration token.
     *
     * @return the FCM token or null if not received from FCM
     */
    @Nullable
    public static String getFcmToken(Context context) {
        return null;
    }

    /**
     * Store FCM token to the SharedPrefs
     *
     * @param context android context
     * @param token   the token to store
     */
    public static void storeFcmToken(@NonNull Context context,
                                     @Nullable String token) {
        // No op
    }

    /**
     * onNewToken may not be called on application upgrade, so ensure my shared pref is set
     *
     * @param activity the first launch Activity
     */
    public static void ensureFcmTokenIsRetrieved(final Activity activity) {
        // No op
    }
}
