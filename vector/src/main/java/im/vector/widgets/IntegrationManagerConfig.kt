/*
 * Copyright 2019-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */
package im.vector.widgets

/**
 * Configuration for an integration manager.
 * By default, it uses URLs defined in the app settings but they can be overridden.
 */
data class IntegrationManagerConfig(
        val uiUrl: String,
        val apiUrl: String,
        val jitsiUrl : String,
        val whiteListedUrls : List<String> = emptyList()
)

