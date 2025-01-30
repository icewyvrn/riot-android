/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package im.vector.util

import java.net.MalformedURLException
import java.net.URL

/**
 * Schemes
 */
private const val HTTP_SCHEME = "http://"
const val HTTPS_SCHEME = "https://"

/**
 * Remove the http schemes from the URl passed in parameter
 *
 * @param aUrl URL to be parsed
 * @return the URL with the scheme removed
 */
fun removeUrlScheme(aUrl: String?): String? {
    var urlRetValue = aUrl

    if (null != aUrl) {
        // remove URL scheme
        if (aUrl.startsWith(HTTP_SCHEME)) {
            urlRetValue = aUrl.substring(HTTP_SCHEME.length)
        } else if (aUrl.startsWith(HTTPS_SCHEME)) {
            urlRetValue = aUrl.substring(HTTPS_SCHEME.length)
        }
    }

    return urlRetValue
}

fun extractDomain(aUrl: String?): String? {
    try {
        return aUrl?.let {  URL(it).host }
    } catch (e : MalformedURLException) {
        return null
    }
}