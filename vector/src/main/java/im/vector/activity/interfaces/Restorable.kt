/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */

package im.vector.activity.interfaces

import android.os.Bundle

/**
 * Implement this to let the Activity save your internal state when it is destroyed
 */
interface Restorable {

    /**
     * Save internal state
     */
    fun saveState(outState: Bundle)
}