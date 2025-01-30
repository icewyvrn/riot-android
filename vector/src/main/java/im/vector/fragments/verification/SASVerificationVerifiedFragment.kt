/*
 * Copyright 2019-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */
package im.vector.fragments.verification

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import butterknife.OnClick
import im.vector.R
import im.vector.fragments.VectorBaseFragment

class SASVerificationVerifiedFragment : VectorBaseFragment() {

    override fun getLayoutResId() = R.layout.fragment_sas_verification_verified

    companion object {
        fun newInstance() = SASVerificationVerifiedFragment()
    }

    private lateinit var viewModel: SasVerificationViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = activity?.run {
            ViewModelProviders.of(this).get(SasVerificationViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

    }

    @OnClick(R.id.sas_verification_verified_done_button)
    fun onDone() {
        viewModel.finishSuccess()
    }
}