/*
 * Copyright 2019-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */
package im.vector.fragments.roomwidgets

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.webkit.PermissionRequest
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import im.vector.R

object WebviewPermissionUtils {

    @SuppressLint("NewApi")
    fun promptForPermissions(@StringRes title: Int, request: PermissionRequest, activity: Activity) {
        if (activity.isFinishing || activity.isDestroyed) return
        val allowedPermissions = request.resources.map {
            it to false
        }.toMutableList()
        AlertDialog.Builder(activity)
                .setTitle(title)
                .setMultiChoiceItems(
                        request.resources.map { webPermissionToHumanReadable(it, activity) }.toTypedArray()
                        , null
                ) { dialog, which, isChecked ->
                    allowedPermissions[which] = allowedPermissions[which].first to isChecked
                }
                .setPositiveButton(R.string.room_widget_resource_grant_permission) { dialog, wich ->
                    request.grant(allowedPermissions.mapNotNull { perm ->
                        perm.first.takeIf { perm.second }
                    }.toTypedArray())
                }
                .setNegativeButton(R.string.room_widget_resource_decline_permission) { dialog, wich ->
                    request.deny()
                }
                .show()
    }

    private fun webPermissionToHumanReadable(permission: String, context: Context): String {
        return when (permission) {
            PermissionRequest.RESOURCE_AUDIO_CAPTURE      -> context.getString(R.string.room_widget_webview_access_microphone)
            PermissionRequest.RESOURCE_VIDEO_CAPTURE      -> context.getString(R.string.room_widget_webview_access_camera)
            PermissionRequest.RESOURCE_PROTECTED_MEDIA_ID -> context.getString(R.string.room_widget_webview_read_protected_media)
            else                                          -> permission
        }
    }

}