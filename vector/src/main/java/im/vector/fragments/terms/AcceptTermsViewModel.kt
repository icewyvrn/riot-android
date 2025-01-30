/*
 * Copyright 2019-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */
package im.vector.fragments.terms

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import im.vector.R
import im.vector.util.state.MxAsync
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.core.Log
import org.matrix.androidsdk.core.callback.ApiCallback
import org.matrix.androidsdk.core.model.MatrixError
import org.matrix.androidsdk.features.terms.GetTermsResponse
import org.matrix.androidsdk.features.terms.TermsManager

class AcceptTermsViewModel : ViewModel() {

    lateinit var termsArgs: ServiceTermsArgs

    val termsList: MutableLiveData<MxAsync<List<Term>>> = MutableLiveData()

    val acceptTerms: MutableLiveData<MxAsync<Unit>> = MutableLiveData()

    var mxSession: MXSession? = null
    var termsManager: TermsManager? = null

    fun markTermAsAccepted(url: String, accepted: Boolean) {
        termsList.value?.invoke()?.map {
            if (it.url == url) {
                it.copy(accepted = accepted)
            } else it
        }?.let {
            termsList.postValue(MxAsync.Success(it))
        }
    }

    fun initSession(session: MXSession?) {
        mxSession = session
        termsManager = mxSession?.termsManager
    }

    fun acceptTerms() {
        val acceptedTerms = termsList.value?.invoke() ?: return

        acceptTerms.postValue(MxAsync.Loading())
        val agreedUrls = acceptedTerms.map { it.url }

        termsManager?.agreeToTerms(termsArgs.type,
                termsArgs.baseURL,
                agreedUrls,
                termsArgs.token,
                object : ApiCallback<Unit> {
                    override fun onSuccess(info: Unit) {
                        acceptTerms.postValue(MxAsync.Success(Unit))
                    }

                    override fun onUnexpectedError(e: java.lang.Exception?) {
                        acceptTerms.postValue(MxAsync.Error(R.string.unknown_error))
                        Log.e(LOG_TAG, "Failed to agree to terms ", e)
                    }

                    override fun onNetworkError(e: java.lang.Exception?) {
                        acceptTerms.postValue(MxAsync.Error(R.string.unknown_error))
                        Log.e(LOG_TAG, "Failed to agree to terms ", e)
                    }

                    override fun onMatrixError(e: MatrixError?) {
                        acceptTerms.postValue(MxAsync.Error(R.string.unknown_error))
                        Log.e(LOG_TAG, "Failed to agree to terms " + e?.message)
                    }
                }
        )
    }

    fun loadTerms(preferredLanguageCode: String) {
        termsList.postValue(MxAsync.Loading())

        termsManager?.get(termsArgs.type, termsArgs.baseURL, object : ApiCallback<GetTermsResponse> {
            override fun onSuccess(info: GetTermsResponse) {

                val terms = info.serverResponse.getLocalizedTerms(preferredLanguageCode).map {
                    Term(it.localizedUrl ?: "",
                            it.localizedName ?: "",
                            it.version,
                            accepted = info.alreadyAcceptedTermUrls.contains(it.localizedUrl)
                    )
                }
                termsList.postValue(MxAsync.Success(terms))
            }


            override fun onUnexpectedError(e: Exception?) {
                termsList.postValue(MxAsync.Error(R.string.unknown_error))
            }

            override fun onNetworkError(e: Exception?) {
                termsList.postValue(MxAsync.Error(R.string.unknown_error))
            }

            override fun onMatrixError(e: MatrixError?) {
                termsList.postValue(MxAsync.Error(R.string.unknown_error))
            }

        })
    }

    companion object {
        private val LOG_TAG = AcceptTermsViewModel::javaClass.name
    }

}

data class Term(
        val url: String,
        val name: String,
        val version: String? = null,
        val accepted: Boolean = false
)