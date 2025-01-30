/*
 * Copyright 2019-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */
package im.vector.fragments.verification

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import butterknife.BindView
import butterknife.OnClick
import im.vector.R
import im.vector.fragments.VectorBaseFragment
import im.vector.util.VectorUtils
import org.matrix.androidsdk.crypto.verification.IncomingSASVerificationTransaction

class SASVerificationIncomingFragment : VectorBaseFragment() {

    companion object {
        fun newInstance() = SASVerificationIncomingFragment()
    }

    @BindView(R.id.sas_incoming_request_user_display_name)
    lateinit var otherUserDisplayNameTextView: TextView

    @BindView(R.id.sas_incoming_request_user_id)
    lateinit var otherUserIdTextView: TextView

    @BindView(R.id.sas_incoming_request_user_device)
    lateinit var otherDeviceTextView: TextView

    @BindView(R.id.sas_incoming_request_user_avatar)
    lateinit var avatarImageView: ImageView

    override fun getLayoutResId() = R.layout.fragment_sas_verification_incoming_request

    private lateinit var viewModel: SasVerificationViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = activity?.run {
            ViewModelProviders.of(this).get(SasVerificationViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        otherUserDisplayNameTextView.text = viewModel.otherUser?.displayname ?: viewModel.otherUserId
        otherUserIdTextView.text = viewModel.otherUserId
        otherDeviceTextView.text = viewModel.otherDeviceId

        viewModel.otherUser?.let {
            VectorUtils.loadUserAvatar(this.context, viewModel.session, avatarImageView, it.avatarUrl, it.user_id, it.displayname)
        }

        viewModel.transactionState.observe(this, Observer {
            val uxState = (viewModel.transaction as? IncomingSASVerificationTransaction)?.uxState
            when (uxState) {
                IncomingSASVerificationTransaction.State.SHOW_ACCEPT -> {
                    viewModel.loadingLiveEvent.value = null
                }
                IncomingSASVerificationTransaction.State.WAIT_FOR_KEY_AGREEMENT -> {
                    viewModel.loadingLiveEvent.value = R.string.sas_waiting_for_partner
                }
                IncomingSASVerificationTransaction.State.SHOW_SAS -> {
                    viewModel.shortCodeReady()
                }
                IncomingSASVerificationTransaction.State.CANCELLED_BY_ME,
                IncomingSASVerificationTransaction.State.CANCELLED_BY_OTHER -> {
                    viewModel.loadingLiveEvent.value = null
                    viewModel.navigateCancel()
                }
                else -> Unit
            }
        })

    }

    @OnClick(R.id.sas_request_continue_button)
    fun didAccept() {
        viewModel.acceptTransaction()
    }

    @OnClick(R.id.sas_request_cancel_button)
    fun didCancel() {
        viewModel.cancelTransaction()
    }
}