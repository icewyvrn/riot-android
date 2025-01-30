/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package im.vector.activity.policies

import android.view.View
import com.airbnb.epoxy.TypedEpoxyController
import org.matrix.androidsdk.rest.model.login.LocalizedFlowDataLoginTerms

class PolicyController(val listener: PolicyControllerListener) : TypedEpoxyController<List<LocalizedFlowDataLoginTermsChecked>>() {

    override fun buildModels(data: List<LocalizedFlowDataLoginTermsChecked>) {
        data.forEach { entry ->
            policy {
                id(entry.localizedFlowDataLoginTerms.policyName)
                checked(entry.checked)
                title(entry.localizedFlowDataLoginTerms.localizedName!!)

                clickListener(View.OnClickListener { listener.openPolicy(entry.localizedFlowDataLoginTerms) })
                checkChangeListener { _, isChecked ->
                    listener.setChecked(entry.localizedFlowDataLoginTerms, isChecked)
                }
            }
        }
    }

    interface PolicyControllerListener {
        fun setChecked(localizedFlowDataLoginTerms: LocalizedFlowDataLoginTerms, isChecked: Boolean)
        fun openPolicy(localizedFlowDataLoginTerms: LocalizedFlowDataLoginTerms)
    }
}