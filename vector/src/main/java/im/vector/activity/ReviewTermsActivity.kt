/*
 * Copyright 2019-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */
package im.vector.activity

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import im.vector.R
import im.vector.fragments.terms.AcceptTermsFragment
import im.vector.fragments.terms.AcceptTermsViewModel
import im.vector.fragments.terms.ServiceTermsArgs
import org.matrix.androidsdk.features.terms.TermsManager


class ReviewTermsActivity : SimpleFragmentActivity() {

    override fun initUiAndData() {
        super.initUiAndData()
        if (supportFragmentManager.fragments.isEmpty()) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, AcceptTermsFragment.newInstance())
                    .commitNow()
        }

        val viewModel = ViewModelProviders.of(this).get(AcceptTermsViewModel::class.java)
        viewModel.termsArgs = intent.getParcelableExtra(EXTRA_INFO)

        mSession = getSession(intent)

        viewModel.initSession(session)
    }

    companion object {

        private const val EXTRA_INFO = "EXTRA_INFO"

        fun intent(context: Context, serviceType: TermsManager.ServiceType, baseUrl: String, token: String?): Intent {
            return Intent(context, ReviewTermsActivity::class.java).also {
                it.putExtra(EXTRA_INFO, ServiceTermsArgs(serviceType, baseUrl, token))
            }
        }
    }
}