/*
 * Copyright 2019-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package im.vector.features.logout

import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import im.vector.R
import im.vector.activity.VectorHomeActivity
import im.vector.util.PreferencesManager
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.crypto.cryptostore.db.hash


class ProposeLogout(private val session: MXSession,
                    private val activity: VectorHomeActivity) {

    private val preferences = PreferenceManager.getDefaultSharedPreferences(activity)

    fun process() {
        detectUpgrade()

        maybeShowDialog()
    }

    private fun detectUpgrade() {
        val version = preferences.getInt(PreferencesManager.VERSION_BUILD, 0)

        if (version in 1..90300) {
            // This is an upgrade, check identity server value
            val identityServerUrl = session.homeServerConfig.identityServerUri.toString()
            val homeServerUrl = session.homeServerConfig.homeserverUri.toString()

            if ((identityServerUrl == "https://matrix.org" || identityServerUrl == "https://vector.im")
                    && (homeServerUrl == "https://matrix.org" || homeServerUrl.endsWith("modular.im"))) {
                // We can skip the dialog
            } else {
                preferences.edit {
                    putString(ACCESS_TOKEN_HASH, session.homeServerConfig.credentials.accessToken.hash())
                }
            }
        }
    }

    private fun maybeShowDialog() {
        if (preferences.getString(ACCESS_TOKEN_HASH, "") == session.homeServerConfig.credentials.accessToken.hash()) {
            // Prompt the user to perform a logout
            showDialog()
        }
    }

    private fun showDialog() {
        val v = LayoutInflater.from(activity).inflate(R.layout.dialog_html_text, null)

        v.findViewById<TextView>(R.id.dialog_text).text = activity.getString(R.string.security_warning_identity_server,
                session.homeServerConfig.identityServerUri.toString(),
                session.homeServerConfig.identityServerUri.toString())

        AlertDialog.Builder(activity)
                .setTitle(R.string.dialog_title_warning)
                .setIcon(R.drawable.vector_warning_red)
                .setView(v)
                .setCancelable(false)
                .setPositiveButton(R.string.ssl_logout_account) { _, _ ->
                    activity.signOut(false)
                }
                .setNegativeButton(R.string.ignore) { _, _ ->
                    preferences.edit {
                        remove(ACCESS_TOKEN_HASH)
                    }
                }
                .show()
    }

    companion object {
        const val ACCESS_TOKEN_HASH = "ACCESS_TOKEN_HASH"
    }
}