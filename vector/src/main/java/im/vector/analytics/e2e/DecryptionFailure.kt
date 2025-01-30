/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */

package im.vector.analytics.e2e

import java.util.*

/**
 * Failure reasons as defined in https://docs.google.com/document/d/1es7cTCeJEXXfRCTRgZerAM2Wg5ZerHjvlpfTW-gsOfI.
 */
enum class DecryptionFailureReason(val value: String) {
    UNSPECIFIED("unspecified"),
    OLM_KEYS_NOT_SENT("olmKeysNotSent"),
    OLM_INDEX_ERROR("olmIndexError"),
    UNEXPECTED("unexpected")
}


/**
 * This class represents a decryption failure to be reported
 */
data class DecryptionFailure(val reason: DecryptionFailureReason,
                             val failedEventId: String) {

    val timestamp: Long = Date().time

}


