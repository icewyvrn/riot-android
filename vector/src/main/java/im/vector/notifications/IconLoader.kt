/*
 * Copyright 2019-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package im.vector.notifications

import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import androidx.annotation.WorkerThread
import androidx.core.graphics.drawable.IconCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions
import org.matrix.androidsdk.core.Log

/**
 * FIXME It works, but it does not refresh the notification, when it's already displayed
 */
class IconLoader(val context: Context,
                 val listener: IconLoaderListener) {

    /**
     * Avatar Url -> Icon
     */
    private val cache = HashMap<String, IconCompat>()

    // URLs to load
    private val toLoad = HashSet<String>()

    // Black list of URLs (broken URL, etc.)
    private val blacklist = HashSet<String>()

    private var uiHandler = Handler()

    private val handlerThread: HandlerThread = HandlerThread("IconLoader", Thread.MIN_PRIORITY)
    private var backgroundHandler: Handler

    init {
        handlerThread.start()
        backgroundHandler = Handler(handlerThread.looper)
    }

    /**
     * Get icon of a user.
     * If already in cache, use it, else load it and call IconLoaderListener.onIconsLoaded() when ready
     */
    fun getUserIcon(path: String?): IconCompat? {
        if (path == null || path.isEmpty()) {
            return null
        }

        synchronized(cache) {
            if (cache[path] != null) {
                return cache[path]
            }

            // Add to the queue, if not blacklisted
            if (!blacklist.contains(path)) {
                if (toLoad.contains(path)) {
                    // Wait
                } else {
                    toLoad.add(path)

                    backgroundHandler.post {
                        loadUserIcon(path)
                    }
                }
            }
        }

        return null
    }

    @WorkerThread
    private fun loadUserIcon(path: String) {
        val iconCompat = loadBitmap(path)

        synchronized(cache) {
            if (iconCompat == null) {
                // Add to the blacklist
                blacklist.add(path)
            } else {
                cache[path] = iconCompat
            }

            toLoad.remove(path)

            if (toLoad.isEmpty()) {
                uiHandler.post {
                    listener.onIconsLoaded()
                }
            }
        }
    }

    private fun loadBitmap(path: String): IconCompat? {
        try {
            Glide.with(context)
                    .asBitmap()
                    .load(path)
                    .apply(RequestOptions.circleCropTransform().format(DecodeFormat.PREFER_ARGB_8888))
                    .submit()
                    .get()?.let { bitmap ->
                        return IconCompat.createWithBitmap(bitmap)
                    }
        } catch (e: Exception) {
            Log.e("IconLoader", "decodeFile failed", e)
        }
        return null
    }


    interface IconLoaderListener {
        fun onIconsLoaded()
    }
}