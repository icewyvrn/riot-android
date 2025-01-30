/*
 * Copyright 2019-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */
package im.vector.notifications

import android.content.Context
import im.vector.Matrix
import org.matrix.androidsdk.core.Log

class OutdatedEventDetector(val context: Context) {

    /**
     * Returns true if the given event is outdated.
     * Used to clean up notifications if a displayed message has been read on an
     * other device.
     */
    fun isMessageOutdated(notifiableEvent: NotifiableEvent): Boolean {
        if (notifiableEvent is NotifiableMessageEvent) {
            val eventID = notifiableEvent.eventId
            val roomID = notifiableEvent.roomId
            Matrix.getMXSession(context.applicationContext, notifiableEvent.matrixID)?.let { session ->
                //find the room
                if (session.isAlive) {
                    session.dataHandler.getRoom(roomID)?.let { room ->
                        if (room.isEventRead(eventID)) {
                            Log.d(LOG_TAG, "Notifiable Event $eventID is read, and should be removed")
                            return true
                        }
                    }
                }
            }
        }
        return false
    }

    companion object {
        private val LOG_TAG = OutdatedEventDetector::class.java.simpleName
    }
}