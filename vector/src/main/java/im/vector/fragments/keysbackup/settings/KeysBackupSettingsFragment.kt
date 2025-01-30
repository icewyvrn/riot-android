/*
 * Copyright 2019-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */
package im.vector.fragments.keysbackup.settings

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import butterknife.BindView
import im.vector.R
import im.vector.activity.KeysBackupRestoreActivity
import im.vector.activity.KeysBackupSetupActivity
import im.vector.activity.util.WaitingViewData
import im.vector.fragments.VectorBaseFragment
import org.matrix.androidsdk.crypto.keysbackup.KeysBackupStateManager

class KeysBackupSettingsFragment : VectorBaseFragment(),
        KeysBackupSettingsRecyclerViewAdapter.AdapterListener {


    companion object {
        fun newInstance() = KeysBackupSettingsFragment()
    }

    override fun getLayoutResId() = R.layout.fragment_keys_backup_settings

    private lateinit var viewModel: KeysBackupSettingsViewModel

    @BindView(R.id.keys_backup_settings_recycler_view)
    lateinit var recyclerView: androidx.recyclerview.widget.RecyclerView

    private var recyclerViewAdapter: KeysBackupSettingsRecyclerViewAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager

        recyclerViewAdapter = KeysBackupSettingsRecyclerViewAdapter(activity!!)
        recyclerView.adapter = recyclerViewAdapter
        recyclerViewAdapter?.adapterListener = this


        viewModel = activity?.run {
            ViewModelProviders.of(this).get(KeysBackupSettingsViewModel::class.java)
        } ?: throw Exception("Invalid Activity")


        viewModel.keyBackupState.observe(this, Observer { keysBackupState ->
            if (keysBackupState == null) {
                //Cannot happen?
                viewModel.keyVersionTrust.value = null
            } else {
                when (keysBackupState) {
                    KeysBackupStateManager.KeysBackupState.Unknown,
                    KeysBackupStateManager.KeysBackupState.CheckingBackUpOnHomeserver -> {
                        viewModel.loadingEvent.value = WaitingViewData(context!!.getString(R.string.keys_backup_settings_checking_backup_state))
                    }
                    else -> {
                        viewModel.loadingEvent.value = null
                        //All this cases will be manage by looking at the backup trust object
                        viewModel.session?.crypto?.keysBackup?.mKeysBackupVersion?.let {
                            viewModel.getKeysBackupTrust(it)
                        } ?: run {
                            viewModel.keyVersionTrust.value = null
                        }
                    }
                }
            }

            // Update the adapter for each state change
            viewModel.session?.let { session ->
                recyclerViewAdapter?.updateWithTrust(session, viewModel.keyVersionTrust.value)
            }
        })

        viewModel.keyVersionTrust.observe(this, Observer {
            viewModel.session?.let { session ->
                recyclerViewAdapter?.updateWithTrust(session, it)
            }
        })
    }

    override fun didSelectSetupMessageRecovery() {
        context?.let {
            startActivity(KeysBackupSetupActivity.intent(it, viewModel.session?.myUserId
                    ?: "", false))
        }
    }

    override fun didSelectRestoreMessageRecovery() {
        context?.let {
            startActivity(KeysBackupRestoreActivity.intent(it, viewModel.session?.myUserId ?: ""))
        }
    }

    override fun didSelectDeleteSetupMessageRecovery() {
        activity?.let {
            AlertDialog.Builder(it)
                    .setTitle(R.string.keys_backup_settings_delete_confirm_title)
                    .setMessage(R.string.keys_backup_settings_delete_confirm_message)
                    .setCancelable(false)
                    .setPositiveButton(R.string.keys_backup_settings_delete_confirm_title) { _, _ ->
                        viewModel.deleteCurrentBackup(it)
                    }
                    .setNegativeButton(R.string.cancel, null)
                    .setCancelable(true)
                    .show()
        }
    }

}