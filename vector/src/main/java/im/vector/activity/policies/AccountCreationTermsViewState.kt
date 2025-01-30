/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */

package im.vector.activity.policies

import org.matrix.androidsdk.rest.model.login.LocalizedFlowDataLoginTerms

data class AccountCreationTermsViewState(
        val localizedFlowDataLoginTermsChecked: List<LocalizedFlowDataLoginTermsChecked>
) {
    fun check(data: LocalizedFlowDataLoginTerms) {
        localizedFlowDataLoginTermsChecked.find { it.localizedFlowDataLoginTerms == data }?.checked = true
    }

    fun uncheck(data: LocalizedFlowDataLoginTerms) {
        localizedFlowDataLoginTermsChecked.find { it.localizedFlowDataLoginTerms == data }?.checked = false
    }

    fun allChecked(): Boolean {
        localizedFlowDataLoginTermsChecked.forEach {
            if (!it.checked) {
                return false
            }
        }

        // Ok
        return true
    }
}