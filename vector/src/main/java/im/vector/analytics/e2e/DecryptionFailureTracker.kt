/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package im.vector.analytics.e2e

import im.vector.analytics.Analytics
import im.vector.analytics.TrackingEvent
import org.matrix.androidsdk.crypto.MXCryptoError
import org.matrix.androidsdk.data.RoomState
import org.matrix.androidsdk.listeners.MXEventListener
import org.matrix.androidsdk.rest.model.Event
import org.matrix.androidsdk.rest.model.RoomMember
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.ArrayList
import kotlin.concurrent.timer

/**
 * This class is responsible for managing the reported decryption errors
 */
class DecryptionFailureTracker(
        private val analytics: Analytics) : MXEventListener() {

    private val reportedFailures = ConcurrentHashMap<String, DecryptionFailure>()
    private val trackedEvents = HashSet<String>()
    private val checkFailuresTimer = timer(period = CHECK_PERIOD) {
        checkFailures()
    }


    /**
     * Reports the decryption error to the tracker.
     * The error will be filtered.
     * @param event: The error event
     * @param roomState: The state of the room when the decryption error happened
     * @param userId: The user id of the current session
     */
    fun reportUnableToDecryptError(event: Event, roomState: RoomState, userId: String) {
        if (reportedFailures[event.eventId] != null || trackedEvents.contains(event.eventId)) {
            return
        }
        // Filter out "expected" UTDs
        // We cannot decrypt messages sent before the user joined the room
        val myUser = roomState.getMember(userId)
        if (myUser == null || myUser.membership != RoomMember.MEMBERSHIP_JOIN) {
            return
        }
        val reason = when (event.cryptoError.errcode) {
            MXCryptoError.UNKNOWN_INBOUND_SESSION_ID_ERROR_CODE -> DecryptionFailureReason.OLM_KEYS_NOT_SENT
            MXCryptoError.OLM_ERROR_CODE -> DecryptionFailureReason.OLM_INDEX_ERROR
            MXCryptoError.ENCRYPTING_NOT_ENABLED_ERROR_CODE -> DecryptionFailureReason.UNEXPECTED
            MXCryptoError.UNABLE_TO_DECRYPT_ERROR_CODE -> DecryptionFailureReason.UNEXPECTED
            else -> DecryptionFailureReason.UNSPECIFIED
        }
        val decryptionFailure = DecryptionFailure(reason, event.eventId)
        reportedFailures[event.eventId] = decryptionFailure
    }

    /**
     * Forces to check failures immediately
     */
    fun dispatch() {
        checkFailures()
    }

    /**
     * Reacts when an event is decrypted.
     * Potentially removes a previously reported failure
     * @param roomId the room ID of the event who is decrypted
     * @param eventId the event ID of the event who is decrypted
     */
    override fun onEventDecrypted(roomId: String?, eventId: String?) {
        reportedFailures.remove(eventId)
    }

    //region Private methods

    private fun checkFailures() {
        val now = Date().time
        val failuresToTrack = ArrayList<DecryptionFailure>()
        for (reportedFailure in reportedFailures.values) {
            if (reportedFailure.timestamp < now - GRACE_PERIOD) {
                failuresToTrack.add(reportedFailure)
                reportedFailures.remove(reportedFailure.failedEventId)
                trackedEvents.add(reportedFailure.failedEventId)
            }
        }
        failuresToTrack
                .groupingBy { it.reason }
                .eachCount()
                .forEach {
                    analytics.trackEvent(TrackingEvent.DecryptionFailure(it.component1(), it.component2()))
                }
    }

    //endregion

    companion object {
        // Period in ms before checkFailures is called
        private const val CHECK_PERIOD = 5 * 1000L
        // Give events a chance to be decrypted by waiting `GRACE_PERIOD` before counting
        // and reporting them as failures (in ms)
        private const val GRACE_PERIOD = 60 * 1000L
    }


}