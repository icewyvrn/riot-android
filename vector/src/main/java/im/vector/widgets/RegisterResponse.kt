/*
 * Copyright 2019-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package im.vector.widgets

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
        @JvmField
        @SerializedName("scalar_token")
        val scalarToken: String?
)
