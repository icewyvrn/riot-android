/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package im.vector.receiver

import android.content.*
import android.util.Log
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import im.vector.Matrix
import im.vector.util.lsFiles

/**
 * Receiver to handle some command from ADB
 */
class DebugReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(LOG_TAG, "Received debug action: ${intent.action}")

        when (intent.action) {
            DEBUG_ACTION_DUMP_FILESYSTEM    -> lsFiles(context)
            DEBUG_ACTION_DUMP_PREFERENCES   -> dumpPreferences(context)
            DEBUG_ACTION_ALTER_SCALAR_TOKEN -> alterScalarToken(context)
            DEBUG_ACTION_ALTER_AUTH_TOKEN   -> alterAuthToken(context)
        }
    }

    private fun dumpPreferences(context: Context) {
        logPrefs("DefaultSharedPreferences", PreferenceManager.getDefaultSharedPreferences(context))
        logPrefs("Vector.LoginStorage", context.getSharedPreferences("Vector.LoginStorage", Context.MODE_PRIVATE))
        logPrefs("PushManager", context.getSharedPreferences("GcmRegistrationManager", Context.MODE_PRIVATE))
    }

    private fun logPrefs(name: String, sharedPreferences: SharedPreferences?) {
        Log.d(LOG_TAG, "SharedPreferences $name:")

        sharedPreferences?.let { prefs ->
            prefs.all.keys.forEach { key ->
                Log.d(LOG_TAG, "$key : ${prefs.all[key]}")
            }
        }
    }

    private fun alterScalarToken(context: Context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit {
            putString("SCALAR_TOKEN_PREFERENCE_KEY" + Matrix.getInstance(context).defaultSession.myUserId, "bad_token")
        }
    }

    private fun alterAuthToken(context: Context) {
        val session = Matrix.getInstance(context).defaultSession
        val alteredAccessToken = session.credentials.accessToken + "-ALT343D"
        session.credentials.accessToken = alteredAccessToken
        // session.cryptoRestClient.setAccessToken(alteredAccessToken)
        session.accountDataRestClient.setAccessToken(alteredAccessToken)
        session.callRestClient.setAccessToken(alteredAccessToken)
        session.pushersRestClient.setAccessToken(alteredAccessToken)
        session.filterRestClient.setAccessToken(alteredAccessToken)
        session.mediaScanRestClient.setAccessToken(alteredAccessToken)
        // session.roomKeysRestClient.setAccessToken(alteredAccessToken)


        val loginStorage = Matrix.getInstance(context)!!.loginStorage
        loginStorage.replaceCredentials(session.homeServerConfig)
    }


    companion object {
        private const val LOG_TAG = "DebugReceiver"

        private const val DEBUG_ACTION_DUMP_FILESYSTEM = "im.vector.receiver.DEBUG_ACTION_DUMP_FILESYSTEM"
        private const val DEBUG_ACTION_DUMP_PREFERENCES = "im.vector.receiver.DEBUG_ACTION_DUMP_PREFERENCES"
        private const val DEBUG_ACTION_ALTER_SCALAR_TOKEN = "im.vector.receiver.DEBUG_ACTION_ALTER_SCALAR_TOKEN"
        private const val DEBUG_ACTION_ALTER_AUTH_TOKEN = "im.vector.receiver.DEBUG_ACTION_ALTER_AUTH_TOKEN"

        fun getIntentFilter() = IntentFilter().apply {
            addAction(DEBUG_ACTION_DUMP_FILESYSTEM)
            addAction(DEBUG_ACTION_DUMP_PREFERENCES)
            addAction(DEBUG_ACTION_ALTER_SCALAR_TOKEN)
            addAction(DEBUG_ACTION_ALTER_AUTH_TOKEN)
        }
    }
}
