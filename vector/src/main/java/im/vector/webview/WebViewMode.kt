/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */

package im.vector.webview

import im.vector.activity.VectorAppCompatActivity

/**
 * This enum indicates the WebView mode. It's responsible for creating a WebViewEventListener
 */
enum class WebViewMode : WebViewEventListenerFactory {

    DEFAULT {
        override fun eventListener(activity: VectorAppCompatActivity): WebViewEventListener {
            return DefaultWebViewEventListener()
        }
    },
    CONSENT {
        override fun eventListener(activity: VectorAppCompatActivity): WebViewEventListener {
            return ConsentWebViewEventListener(activity, DefaultWebViewEventListener())
        }
    };

}