/*
 * Copyright 2019-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */
package im.vector.fragments.verification

import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.transition.TransitionManager
import butterknife.BindView
import butterknife.OnClick
import im.vector.R
import im.vector.activity.CommonActivityUtils
import im.vector.fragments.VectorBaseFragment
import im.vector.listeners.YesNoListener
import org.matrix.androidsdk.core.callback.SimpleApiCallback
import org.matrix.androidsdk.crypto.data.MXDeviceInfo
import org.matrix.androidsdk.crypto.verification.OutgoingSASVerificationRequest

class SASVerificationStartFragment : VectorBaseFragment() {

    companion object {
        fun newInstance() = SASVerificationStartFragment()
    }

    override fun getLayoutResId() = R.layout.fragment_sas_verification_start

    private lateinit var viewModel: SasVerificationViewModel


    @BindView(R.id.rootLayout)
    lateinit var rootLayout: ViewGroup

    @BindView(R.id.sas_start_button)
    lateinit var startButton: Button

    @BindView(R.id.sas_start_button_loading)
    lateinit var startButtonLoading: ProgressBar

    @BindView(R.id.sas_verifying_keys)
    lateinit var loadingText: TextView


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = activity?.run {
            ViewModelProviders.of(this).get(SasVerificationViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        viewModel.transactionState.observe(this, Observer {
            val uxState = (viewModel.transaction as? OutgoingSASVerificationRequest)?.uxState
            when (uxState) {
                OutgoingSASVerificationRequest.State.WAIT_FOR_KEY_AGREEMENT -> {
                    //display loading
                    TransitionManager.beginDelayedTransition(this.rootLayout)
                    this.loadingText.isVisible = true
                    this.startButton.isInvisible = true
                    this.startButtonLoading.isVisible = true
                    this.startButtonLoading.animate()

                }
                OutgoingSASVerificationRequest.State.SHOW_SAS -> {
                    viewModel.shortCodeReady()
                }
                OutgoingSASVerificationRequest.State.CANCELLED_BY_ME,
                OutgoingSASVerificationRequest.State.CANCELLED_BY_OTHER -> {
                    viewModel.navigateCancel()
                }
                else -> {
                    TransitionManager.beginDelayedTransition(this.rootLayout)
                    this.loadingText.isVisible = false
                    this.startButton.isVisible = true
                    this.startButtonLoading.isVisible = false
                }
            }
        })

    }

    @OnClick(R.id.sas_start_button)
    fun doStart() {
        viewModel.beginSasKeyVerification()
    }

    @OnClick(R.id.sas_legacy_verification)
    fun doLegacy() {
        viewModel.session.crypto?.getDeviceInfo(viewModel.otherUserId ?: "", viewModel.otherDeviceId
                ?: "", object : SimpleApiCallback<MXDeviceInfo>() {
            override fun onSuccess(info: MXDeviceInfo?) {
                info?.let {

                    CommonActivityUtils.displayDeviceVerificationDialogLegacy(it, it.userId, viewModel.session, activity, object : YesNoListener {
                        override fun yes() {
                            viewModel.manuallyVerified()
                        }

                        override fun no() {

                        }
                    })
                }
            }
        })
    }

    @OnClick(R.id.sas_cancel_button)
    fun doCancel() {
        // Transaction may be started, or not
        viewModel.cancelTransaction()
    }


}