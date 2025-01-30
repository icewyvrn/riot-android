/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */

package im.vector.activity.policies

import org.matrix.androidsdk.rest.model.login.LocalizedFlowDataLoginTerms

data class LocalizedFlowDataLoginTermsChecked(val localizedFlowDataLoginTerms: LocalizedFlowDataLoginTerms,
                                              var checked: Boolean = false)