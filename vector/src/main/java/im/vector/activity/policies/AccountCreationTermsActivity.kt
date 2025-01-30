/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package im.vector.activity.policies

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Parcelable
import android.widget.Button
import butterknife.BindView
import butterknife.OnClick
import com.airbnb.epoxy.EpoxyRecyclerView
import im.vector.R
import im.vector.activity.VectorAppCompatActivity
import im.vector.activity.VectorWebViewActivity
import im.vector.ui.themes.ThemeUtils
import im.vector.webview.WebViewMode
import org.matrix.androidsdk.rest.model.login.LocalizedFlowDataLoginTerms

/**
 * AccountCreationTermsActivity displays the list of policies the user has to accept
 */
class AccountCreationTermsActivity : VectorAppCompatActivity(),
        PolicyController.PolicyControllerListener {

    @BindView(R.id.account_creation_policy_list)
    lateinit var policyList: EpoxyRecyclerView

    @BindView(R.id.account_creation_policy_button_accept)
    lateinit var submitButton: Button

    override fun getLayoutRes() = R.layout.activity_vector_registration_terms

    override fun getTitleRes() = R.string.create_account

    private var accountCreationTermsViewState: AccountCreationTermsViewState = AccountCreationTermsViewState(emptyList())

    private val policyController = PolicyController(this)

    override fun initUiAndData() {
        configureToolbar()

        policyList.setController(policyController)

        val list = ArrayList<LocalizedFlowDataLoginTermsChecked>()

        intent.getParcelableArrayListExtra<LocalizedFlowDataLoginTerms>(DATA)
                .forEach {
                    list.add(LocalizedFlowDataLoginTermsChecked(it))
                }

        accountCreationTermsViewState = AccountCreationTermsViewState(list)

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
            toolbar.setBackgroundColor(ThemeUtils.getColor(this, R.attr.colorAccent))
            submitButton.setBackgroundColor(ThemeUtils.getColor(this, R.attr.colorAccent))
        }

        renderState()
    }

    private fun renderState() {
        policyController.setData(accountCreationTermsViewState.localizedFlowDataLoginTermsChecked)

        // Button is enabled only if all checkboxes are checked
        // TODO Update this when the redesign
        if (accountCreationTermsViewState.allChecked()) {
            submitButton.isEnabled = true
            submitButton.alpha = 1f
        } else {
            submitButton.isEnabled = false
            submitButton.alpha = 0.5f
        }
    }

    override fun setChecked(localizedFlowDataLoginTerms: LocalizedFlowDataLoginTerms, isChecked: Boolean) {
        if (isChecked) {
            accountCreationTermsViewState.check(localizedFlowDataLoginTerms)
        } else {
            accountCreationTermsViewState.uncheck(localizedFlowDataLoginTerms)
        }

        renderState()
    }

    override fun openPolicy(localizedFlowDataLoginTerms: LocalizedFlowDataLoginTerms) {
        val intent = VectorWebViewActivity.getIntent(this,
                localizedFlowDataLoginTerms.localizedUrl!!,
                localizedFlowDataLoginTerms.localizedName!!,
                WebViewMode.DEFAULT)

        startActivity(intent)
    }

    @OnClick(R.id.account_creation_policy_button_accept)
    internal fun submit() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    companion object {
        private const val DATA = "DATA"

        fun getIntent(context: Context, list: List<LocalizedFlowDataLoginTerms>) = Intent(context, AccountCreationTermsActivity::class.java).apply {
            putParcelableArrayListExtra(DATA, list as java.util.ArrayList<out Parcelable>)
        }
    }
}
