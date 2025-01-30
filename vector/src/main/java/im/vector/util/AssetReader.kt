/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package im.vector.util

import android.content.Context
import org.matrix.androidsdk.core.Log
import java.io.InputStreamReader

/**
 * Singleton to read asset files
 */
object AssetReader {

    /* ==========================================================================================
     * CACHE
     * ========================================================================================== */
    private val cache = HashMap<String, String>()

    /**
     * Read an asset from resource and return a String or null in cas of error.
     *
     * @param assetFilename Asset filename
     * @return the content of the asset file
     */
    fun readAssetFile(context: Context, assetFilename: String): String? {
        // Check if it is available in cache
        if (cache.contains(assetFilename)) {
            return cache[assetFilename]
        }

        var assetContent: String? = null

        try {
            val inputStream = context.assets.open(assetFilename)
            val buffer = CharArray(1024)
            val out = StringBuilder()

            val inputStreamReader = InputStreamReader(inputStream, "UTF-8")
            while (true) {
                val rsz = inputStreamReader.read(buffer, 0, buffer.size)
                if (rsz < 0)
                    break
                out.append(buffer, 0, rsz)
            }
            assetContent = out.toString()

            // Keep in cache
            cache[assetFilename] = assetContent

            inputStreamReader.close()
            inputStream.close()
        } catch (e: Exception) {
            Log.e("AssetReader", "## readAssetFile() failed : " + e.message, e)
        }

        return assetContent
    }

    fun clearCache() {
        cache.clear()
    }
}