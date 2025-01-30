/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */

package im.vector.util

enum class AutoCompletionMode {
    USER_MODE,
    COMMAND_MODE;

    companion object {
        /**
         * It's important to start with " " to enter USER_MODE even if text starts with "/"
         */
        fun getWithText(text: String) = when {
            text.startsWith("@") || text.contains(" ") -> USER_MODE
            text.startsWith("/") -> COMMAND_MODE
            else -> USER_MODE
        }
    }
}