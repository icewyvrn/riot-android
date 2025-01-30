/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */

package im.vector.util

import com.google.gson.reflect.TypeToken
import im.vector.types.JsonDict
import org.matrix.androidsdk.core.JsonUtils
import org.matrix.androidsdk.core.Log
import java.util.*


/**
 * Convert an object to a map
 *
 * @return the event as a map, or null in case of failure
 */
fun Any.toJsonMap(): JsonDict<Any>? {
    val gson = JsonUtils.getGson(false)
    var objectAsMap: JsonDict<Any>? = null

    try {
        val stringifiedEvent = gson.toJson(this)
        objectAsMap = gson.fromJson<JsonDict<Any>>(stringifiedEvent, object : TypeToken<HashMap<String, Any>>() {

        }.type)
    } catch (e: Exception) {
        Log.e("TAG", "## Any.toJsonMap() failed " + e.message, e)
    }

    return objectAsMap
}