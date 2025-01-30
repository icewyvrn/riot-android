/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */

package im.vector.webview

import org.matrix.androidsdk.core.Log

private const val TAG = "DefaultWebViewEventListener"

/**
 * This class is the default implementation of WebViewEventListener.
 * It can be used with delegation pattern
 */

class DefaultWebViewEventListener : WebViewEventListener {

    override fun pageWillStart(url: String) {
        Log.v(TAG, "On page will start: $url")
    }

    override fun onPageStarted(url: String) {
        Log.d(TAG, "On page started: $url")
    }

    override fun onPageFinished(url: String) {
        Log.d(TAG, "On page finished: $url")
    }

    override fun onPageError(url: String, errorCode: Int, description: String) {
        Log.e(TAG, "On received error: $url - errorCode: $errorCode - message: $description")
    }

    override fun shouldOverrideUrlLoading(url: String): Boolean {
        Log.v(TAG, "Should override url: $url")
        return false
    }
}