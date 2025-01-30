/*
 * Copyright 2019-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */
package im.vector.fragments.terms

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.matrix.androidsdk.features.terms.TermsManager

@Parcelize
data class ServiceTermsArgs(
        val type: TermsManager.ServiceType,
        val baseURL: String,
        val token: String? = null
) : Parcelable
