/*
 * Copyright 2019-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package im.vector.listeners

/**
 * Simple interface with yes() and no() methods
 */
interface YesNoListener {
    fun yes()

    fun no()
}