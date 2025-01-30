/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */

package im.vector.activity

import android.content.Context
import android.content.Intent
import android.media.ExifInterface
import butterknife.BindView
import com.github.chrisbanes.photoview.PhotoView
import im.vector.R

class VectorAvatarViewerActivity : MXCActionBarActivity() {

    @BindView(R.id.avatar_viewer_image)
    lateinit var photoView: PhotoView

    override fun getLayoutRes() = R.layout.activity_avatar_viewer

    override fun initUiAndData() {
        super.initUiAndData()

        val session = getSession(intent)

        if (session != null) {
            session.mediaCache.loadBitmap(session.homeServerConfig,
                    photoView, intent.getStringExtra(EXTRA_AVATAR_URL), 0, ExifInterface.ORIENTATION_UNDEFINED, null, null)
        } else {
            finish()
        }
    }

    companion object {
        private const val EXTRA_AVATAR_URL = "EXTRA_AVATAR_URL"

        fun getIntent(context: Context, matrixId: String, avatarUrl: String): Intent {
            return Intent(context, VectorAvatarViewerActivity::class.java).apply {
                putExtra(EXTRA_MATRIX_ID, matrixId)
                putExtra(EXTRA_AVATAR_URL, avatarUrl)
            }
        }
    }

    // For transition
    override fun onBackPressed() {
        supportFinishAfterTransition()
    }
}