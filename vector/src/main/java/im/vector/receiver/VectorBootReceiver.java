/*
 * Copyright 2025 New Vector Ltd.
 * Copyright 2017 Vector Creation Ltd
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package im.vector.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import org.matrix.androidsdk.core.Log;

import im.vector.services.EventStreamServiceX;
import im.vector.util.PreferencesManager;

public class VectorBootReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = VectorBootReceiver.class.getSimpleName();

    public static final String PERMANENT_LISTENT = "PERMANENT_LISTENT";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "## onReceive() : " + intent.getAction());

        if (TextUtils.equals(intent.getAction(), Intent.ACTION_BOOT_COMPLETED)
                || TextUtils.equals(intent.getAction(), "android.intent.action.ACTION_BOOT_COMPLETED")) {
            if (PreferencesManager.autoStartOnBoot(context)) {
                Log.d(LOG_TAG, "## onReceive() : starts the application");
                EventStreamServiceX.Companion.onBootComplete(context);
            } else {
                Log.d(LOG_TAG, "## onReceive() : the autostart is disabled");
            }
        } else if (TextUtils.equals(intent.getAction(), PERMANENT_LISTENT)) {
            EventStreamServiceX.Companion.onForcePermanentEventListening(context);
        }
    }
}
