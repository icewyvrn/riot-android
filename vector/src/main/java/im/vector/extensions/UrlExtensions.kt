/*
 * Copyright 2019-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */

package im.vector.extensions

import java.net.URLEncoder

/**
 * Append param and value to a Url, using "?" or "&". Value parameter will be encoded
 * Return this for chaining purpose
 */
fun StringBuilder.appendParamToUrl(param: String, value: String): StringBuilder {
    if (contains("?")) {
        append("&")
    } else {
        append("?")
    }

    append(param)
    append("=")
    append(URLEncoder.encode(value, "utf-8"))

    return this
}