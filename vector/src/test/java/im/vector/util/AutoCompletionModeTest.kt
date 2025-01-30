/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */

package im.vector.util

import org.junit.Assert.assertEquals
import org.junit.Test

class AutoCompletionModeTest {
    @Test
    fun userMode_empty() {
        assertEquals(AutoCompletionMode.USER_MODE, AutoCompletionMode.getWithText(""))
    }

    @Test
    fun userMode_classic() {
        assertEquals(AutoCompletionMode.USER_MODE, AutoCompletionMode.getWithText("Hello test"))
    }

    @Test
    fun userMode_slash() {
        assertEquals(AutoCompletionMode.USER_MODE, AutoCompletionMode.getWithText("Hello /"))
    }

    @Test
    fun userMode_at() {
        assertEquals(AutoCompletionMode.USER_MODE, AutoCompletionMode.getWithText("Hello @"))
        assertEquals(AutoCompletionMode.USER_MODE, AutoCompletionMode.getWithText("Hello @b"))
        assertEquals(AutoCompletionMode.USER_MODE, AutoCompletionMode.getWithText("Hello @be"))
        assertEquals(AutoCompletionMode.USER_MODE, AutoCompletionMode.getWithText("Hello @ben"))
    }

    @Test
    fun userMode_withCommand() {
        assertEquals(AutoCompletionMode.USER_MODE, AutoCompletionMode.getWithText("/invite "))
        assertEquals(AutoCompletionMode.USER_MODE, AutoCompletionMode.getWithText("/invite b"))
        assertEquals(AutoCompletionMode.USER_MODE, AutoCompletionMode.getWithText("/invite be"))
        assertEquals(AutoCompletionMode.USER_MODE, AutoCompletionMode.getWithText("/invite ben"))
    }

    @Test
    fun userMode_withCommand_at() {
        assertEquals(AutoCompletionMode.USER_MODE, AutoCompletionMode.getWithText("/invite @"))
        assertEquals(AutoCompletionMode.USER_MODE, AutoCompletionMode.getWithText("/invite @b"))
        assertEquals(AutoCompletionMode.USER_MODE, AutoCompletionMode.getWithText("/invite @be"))
        assertEquals(AutoCompletionMode.USER_MODE, AutoCompletionMode.getWithText("/invite @ben"))
    }

    @Test
    fun commandMode_empty() {
        assertEquals(AutoCompletionMode.COMMAND_MODE, AutoCompletionMode.getWithText("/"))
    }

    @Test
    fun commandMode_notEmpty() {
        assertEquals(AutoCompletionMode.COMMAND_MODE, AutoCompletionMode.getWithText("/m"))
        assertEquals(AutoCompletionMode.COMMAND_MODE, AutoCompletionMode.getWithText("/me"))
    }
}