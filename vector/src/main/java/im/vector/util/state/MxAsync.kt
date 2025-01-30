/*
 * Copyright 2019-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package im.vector.util.state

import androidx.annotation.StringRes

sealed class MxAsync<out T> {

    open operator fun invoke(): T? = null

    class Loading<out T> : MxAsync<T>()

    data class Error<out T>(@StringRes val stringResId: Int) : MxAsync<T>()

    data class Success<out T>(val value: T) : MxAsync<T>() {
        override operator fun invoke(): T = value
    }

}
