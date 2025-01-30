/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */

package im.vector.webview

import im.vector.activity.VectorAppCompatActivity

interface WebViewEventListenerFactory {

    /**
     * @return an instance of WebViewEventListener
     */
    fun eventListener(activity: VectorAppCompatActivity): WebViewEventListener

}