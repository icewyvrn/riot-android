/*
 * Copyright 2019-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */
package im.vector.activity

/**
 * A fragment should implement this interface if it wants to intercept backPressed events.
 * Any activity extending VectorAppCompatActivity will propagate back pressed event to child
 * fragment that implements it.
 */
interface HandleBackParticipant {

    /**
     * Returns true, if the on back pressed event has been handled by this Fragment.
     * Otherwise return false
     */
    fun onBackPressed(): Boolean

}