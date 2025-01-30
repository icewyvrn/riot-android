/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */

package im.vector.extensions

import android.os.Bundle
import androidx.fragment.app.Fragment

fun Boolean.toOnOff() = if (this) "ON" else "OFF"

/**
 * Apply argument to a Fragment
 */
fun <T : Fragment> T.withArgs(block: Bundle.() -> Unit) = apply { arguments = Bundle().apply(block) }