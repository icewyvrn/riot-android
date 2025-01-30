/*
 * Copyright 2019-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */

package im.vector.receiver

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Parameters extracted from a configuration url
 * Ex: https://riot.im/config/config?hs_url=https%3A%2F%2Fexample.modular.im&is_url=https%3A%2F%2Fcustom.identity.org
 */
@Parcelize
data class LoginConfig(
        val homeServerUrl: String?,
        val identityServerUrl: String?
) : Parcelable {

    companion object {
        fun parse(from: Uri?): LoginConfig? {
            return from?.let {
                LoginConfig(
                        homeServerUrl = it.getQueryParameter("hs_url"),
                        identityServerUrl = it.getQueryParameter("is_url")
                )
            }
        }
    }
}