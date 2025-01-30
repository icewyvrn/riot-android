/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */

package im.vector.dialogs

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import im.vector.R

internal sealed class DialogListItem(@DrawableRes val iconRes: Int,
                                     @StringRes val titleRes: Int) {

    object StartVoiceCall : DialogListItem(R.drawable.voice_call_green, R.string.action_voice_call)
    object StartVideoCall : DialogListItem(R.drawable.video_call_green, R.string.action_video_call)

    object SendFile : DialogListItem(R.drawable.ic_material_file, R.string.option_send_files)
    object SendVoice : DialogListItem(R.drawable.vector_micro_green, R.string.option_send_voice)
    object SendSticker : DialogListItem(R.drawable.ic_send_sticker, R.string.option_send_sticker)
    object TakePhoto : DialogListItem(R.drawable.ic_material_camera, R.string.option_take_photo)
    object TakeVideo : DialogListItem(R.drawable.ic_material_videocam, R.string.option_take_video)
    object TakePhotoVideo : DialogListItem(R.drawable.ic_material_camera, R.string.option_take_photo_video)

}
