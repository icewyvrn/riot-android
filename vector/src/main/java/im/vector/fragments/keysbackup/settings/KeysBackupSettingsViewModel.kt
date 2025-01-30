/*
 * Copyright 2019-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */
package im.vector.fragments.keysbackup.settings

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import im.vector.R
import im.vector.activity.util.WaitingViewData
import im.vector.ui.arch.LiveEvent
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.core.callback.ApiCallback
import org.matrix.androidsdk.core.callback.SuccessCallback
import org.matrix.androidsdk.core.model.MatrixError
import org.matrix.androidsdk.crypto.keysbackup.KeysBackupStateManager
import org.matrix.androidsdk.crypto.keysbackup.KeysBackupVersionTrust
import org.matrix.androidsdk.crypto.model.keys.KeysVersionResult

class KeysBackupSettingsViewModel : ViewModel(),
        KeysBackupStateManager.KeysBackupStateListener {

    var session: MXSession? = null

    var keyVersionTrust: MutableLiveData<KeysBackupVersionTrust> = MutableLiveData()
    var keyBackupState: MutableLiveData<KeysBackupStateManager.KeysBackupState> = MutableLiveData()

    private var _apiResultError: MutableLiveData<LiveEvent<String>> = MutableLiveData()
    val apiResultError: LiveData<LiveEvent<String>>
        get() = _apiResultError

    var loadingEvent: MutableLiveData<WaitingViewData> = MutableLiveData()

    fun initSession(session: MXSession) {
        keyBackupState.value = session.crypto?.keysBackup?.state
        if (this.session == null) {
            this.session = session
            session.crypto
                    ?.keysBackup
                    ?.addListener(this)
        }
    }

    fun getKeysBackupTrust(versionResult: KeysVersionResult) {
        val keysBackup = session?.crypto?.keysBackup
        keysBackup?.getKeysBackupTrust(versionResult, SuccessCallback { info ->
            keyVersionTrust.value = info
        })
    }

    override fun onCleared() {
        super.onCleared()
        session?.crypto?.keysBackup?.removeListener(this)
    }

    override fun onStateChange(newState: KeysBackupStateManager.KeysBackupState) {
        keyBackupState.value = newState
    }

    fun deleteCurrentBackup(context: Context) {
        session?.crypto?.keysBackup?.run {
            loadingEvent.value = WaitingViewData(context.getString(R.string.keys_backup_settings_deleting_backup))
            if (currentBackupVersion != null) {
                deleteBackup(currentBackupVersion!!, object : ApiCallback<Void> {
                    override fun onSuccess(info: Void?) {
                        //mmmm if state is stil unknown/checking..
                        loadingEvent.value = null
                    }

                    override fun onUnexpectedError(e: java.lang.Exception) {
                        loadingEvent.value = null
                        _apiResultError.value = LiveEvent(context.getString(R.string.keys_backup_get_version_error, e.localizedMessage))
                    }

                    override fun onNetworkError(e: java.lang.Exception) {
                        loadingEvent.value = null
                        _apiResultError.value = LiveEvent(context.getString(R.string.network_error_please_check_and_retry))
                    }

                    override fun onMatrixError(e: MatrixError) {
                        loadingEvent.value = null
                        _apiResultError.value = LiveEvent(context.getString(R.string.keys_backup_get_version_error, e.localizedMessage))
                    }

                })
            }
        }
    }
}