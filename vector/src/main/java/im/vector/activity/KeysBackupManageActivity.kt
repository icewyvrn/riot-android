/*
 * Copyright 2019-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */
package im.vector.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import im.vector.R
import im.vector.fragments.keysbackup.settings.KeysBackupSettingsFragment
import im.vector.fragments.keysbackup.settings.KeysBackupSettingsViewModel
import org.matrix.androidsdk.core.callback.ApiCallback
import org.matrix.androidsdk.core.model.MatrixError
import org.matrix.androidsdk.crypto.keysbackup.KeysBackupStateManager

class KeysBackupManageActivity : SimpleFragmentActivity() {

    companion object {

        fun intent(context: Context, matrixID: String): Intent {
            val intent = Intent(context, KeysBackupManageActivity::class.java)
            intent.putExtra(EXTRA_MATRIX_ID, matrixID)
            return intent
        }
    }

    override fun getTitleRes() = R.string.encryption_message_recovery


    private lateinit var viewModel: KeysBackupSettingsViewModel

    override fun initUiAndData() {
        super.initUiAndData()
        viewModel = ViewModelProviders.of(this).get(KeysBackupSettingsViewModel::class.java)
        viewModel.initSession(mSession)


        if (supportFragmentManager.fragments.isEmpty()) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, KeysBackupSettingsFragment.newInstance())
                    .commitNow()

            mSession.crypto
                    ?.keysBackup
                    ?.forceUsingLastVersion(object : ApiCallback<Boolean> {
                        override fun onSuccess(info: Boolean?) {}

                        override fun onUnexpectedError(e: java.lang.Exception?) {}

                        override fun onNetworkError(e: java.lang.Exception?) {}

                        override fun onMatrixError(e: MatrixError?) {}
                    })
        }

        viewModel.loadingEvent.observe(this, Observer {
            updateWaitingView(it)
        })


        viewModel.apiResultError.observe(this, Observer { uxStateEvent ->
            uxStateEvent?.getContentIfNotHandled()?.let {
                AlertDialog.Builder(this)
                        .setTitle(R.string.unknown_error)
                        .setMessage(it)
                        .setCancelable(false)
                        .setPositiveButton(R.string.ok, null)
                        .show()
            }
        })

    }

    override fun onBackPressed() {
        // When there is no network we could get stuck in infinite loading
        // because backup state will stay in CheckingBackUpOnHomeserver
        if (viewModel.keyBackupState.value == KeysBackupStateManager.KeysBackupState.Unknown
                || viewModel.keyBackupState.value == KeysBackupStateManager.KeysBackupState.CheckingBackUpOnHomeserver) {
            finish()
            return
        }
        super.onBackPressed()
    }
}