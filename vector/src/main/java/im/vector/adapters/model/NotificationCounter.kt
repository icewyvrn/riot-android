/*
 * Copyright 2019-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */

package im.vector.adapters.model

data class NotificationCounter(var highlights: Int = 0,
                               var notifications: Int = 0) {
    fun addHighlights(quantity: Int) {
        highlights += quantity
    }

    fun addNotifications(quantity: Int) {
        notifications += quantity
    }
}