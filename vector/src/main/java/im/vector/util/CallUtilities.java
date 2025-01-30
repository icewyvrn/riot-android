/*
 * Copyright 2025 New Vector Ltd.
 * Copyright 2014 OpenMarket Ltd
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package im.vector.util;

import android.content.Context;

import org.matrix.androidsdk.call.IMXCall;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import im.vector.R;

/**
 * This class contains the call toolbox.
 */
public class CallUtilities {
    //
    private static SimpleDateFormat mHourMinSecFormat = null;
    private static SimpleDateFormat mMinSecFormat = null;

    /**
     * Format a time in seconds to a HH:MM:SS string.
     *
     * @param seconds the time in seconds
     * @return the formatted time
     */
    private static String formatSecondsToHMS(long seconds) {
        if (null == mHourMinSecFormat) {
            mHourMinSecFormat = new SimpleDateFormat("HH:mm:ss");
            mHourMinSecFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            mMinSecFormat = new SimpleDateFormat("mm:ss");
            mMinSecFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        }

        if (seconds < 3600) {
            return mMinSecFormat.format(new Date(seconds * 1000));
        } else {
            return mHourMinSecFormat.format(new Date(seconds * 1000));
        }
    }

    /**
     * Return the call status.
     *
     * @param call the dedicated call
     * @return the call status.
     */
    public static String getCallStatus(Context context, IMXCall call) {
        if (null == call) {
            return null;
        }

        String callState = call.getCallState();

        switch (callState) {
            case IMXCall.CALL_STATE_CREATED:
            case IMXCall.CALL_STATE_CREATING_CALL_VIEW:
            case IMXCall.CALL_STATE_READY:
            case IMXCall.CALL_STATE_WAIT_LOCAL_MEDIA:
                if (call.isIncoming()) {
                    if (call.isVideo()) {
                        return context.getString(R.string.incoming_video_call);
                    } else {
                        return context.getString(R.string.incoming_voice_call);
                    }
                }
            case IMXCall.CALL_STATE_INVITE_SENT:
            case IMXCall.CALL_STATE_CONNECTING:
            case IMXCall.CALL_STATE_CREATE_ANSWER:
            case IMXCall.CALL_STATE_WAIT_CREATE_OFFER: {
                return context.getString(R.string.call_connecting);
            }
            case IMXCall.CALL_STATE_RINGING:
                if (call.isIncoming()) {
                    if (call.isVideo()) {
                        return context.getString(R.string.incoming_video_call);
                    } else {
                        return context.getString(R.string.incoming_voice_call);
                    }
                } else {
                    return context.getString(R.string.call_ring);
                }
            case IMXCall.CALL_STATE_CONNECTED:
                long elapsedTime = call.getCallElapsedTime();

                if (elapsedTime < 0) {
                    return context.getString(R.string.call_connected);
                } else {
                    return formatSecondsToHMS(elapsedTime);
                }
            case IMXCall.CALL_STATE_ENDED:
                return context.getString(R.string.call_ended);
        }

        return null;
    }
}
