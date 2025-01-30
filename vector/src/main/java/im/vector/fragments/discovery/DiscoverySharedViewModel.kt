/*
 * Copyright 2019-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */
package im.vector.fragments.discovery

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import im.vector.ui.arch.LiveEvent

class DiscoverySharedViewModel : ViewModel() {

    var navigateEvent = MutableLiveData<LiveEvent<Pair<String, String>>>()

    companion object {
        const val NEW_IDENTITY_SERVER_SET_REQUEST = "NEW_IDENTITY_SERVER_SET_REQUEST"
    }

    fun requestChangeToIdentityServer(server: String) {
        navigateEvent.postValue(LiveEvent(NEW_IDENTITY_SERVER_SET_REQUEST to server))
    }
}