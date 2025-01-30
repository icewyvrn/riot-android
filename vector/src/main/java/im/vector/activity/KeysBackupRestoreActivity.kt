/*
 * Copyright 2019-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */
package im.vector.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import im.vector.R
import im.vector.fragments.keysbackup.restore.KeysBackupRestoreFromKeyFragment
import im.vector.fragments.keysbackup.restore.KeysBackupRestoreFromPassphraseFragment
import im.vector.fragments.keysbackup.restore.KeysBackupRestoreSharedViewModel
import im.vector.fragments.keysbackup.restore.KeysBackupRestoreSuccessFragment

class KeysBackupRestoreActivity : SimpleFragmentActivity() {

    companion object {

        fun intent(context: Context, matrixID: String): Intent {
            val intent = Intent(context, KeysBackupRestoreActivity::class.java)
            intent.putExtra(EXTRA_MATRIX_ID, matrixID)
            return intent
        }
    }

    override fun getTitleRes() = R.string.title_activity_keys_backup_restore

    private lateinit var viewModel: KeysBackupRestoreSharedViewModel

    override fun initUiAndData() {
        super.initUiAndData()
        viewModel = ViewModelProviders.of(this).get(KeysBackupRestoreSharedViewModel::class.java)
        viewModel.initSession(mSession)

        viewModel.keyVersionResult.observe(this, Observer { keyVersion ->

            if (keyVersion != null && supportFragmentManager.fragments.isEmpty()) {
                val isBackupCreatedFromPassphrase = keyVersion.getAuthDataAsMegolmBackupAuthData()?.privateKeySalt != null
                if (isBackupCreatedFromPassphrase) {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.container, KeysBackupRestoreFromPassphraseFragment.newInstance())
                            .commitNow()
                } else {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.container, KeysBackupRestoreFromKeyFragment.newInstance())
                            .commitNow()
                }
            }
        })

        viewModel.keyVersionResultError.observe(this, Observer { uxStateEvent ->
            uxStateEvent?.getContentIfNotHandled()?.let {
                AlertDialog.Builder(this)
                        .setTitle(R.string.unknown_error)
                        .setMessage(it)
                        .setCancelable(false)
                        .setPositiveButton(R.string.ok) { _, _ ->
                            //nop
                            finish()
                        }
                        .show()
            }
        })

        if (viewModel.keyVersionResult.value == null) {
            //We need to fetch from API
            viewModel.getLatestVersion(this)
        }

        viewModel.navigateEvent.observe(this, Observer { uxStateEvent ->
            when (uxStateEvent?.getContentIfNotHandled()) {
                KeysBackupRestoreSharedViewModel.NAVIGATE_TO_RECOVER_WITH_KEY -> {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.container, KeysBackupRestoreFromKeyFragment.newInstance())
                            .addToBackStack(null)
                            .commit()
                }
                KeysBackupRestoreSharedViewModel.NAVIGATE_TO_SUCCESS -> {
                    supportFragmentManager.popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.container, KeysBackupRestoreSuccessFragment.newInstance())
                            .commit()
                }
            }
        })

        viewModel.loadingEvent.observe(this, Observer {
            updateWaitingView(it)
        })

        viewModel.importRoomKeysFinishWithResult.observe(this, Observer {
            it?.getContentIfNotHandled()?.let {
                //set data?
                setResult(Activity.RESULT_OK)
                finish()
            }
        })
    }

}