/*
 * Copyright 2019-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package im.vector.widgets.tokens

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import org.matrix.androidsdk.core.JsonUtils

class TokensStore(context: Context) {

    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    private val gson = JsonUtils.getBasicGson()

    private data class TokensStore(
            // Keys are user Id
            @JvmField
            val userToServerTokens: MutableMap<String, ServerTokens> = mutableMapOf()
    )

    private data class ServerTokens(
            // Keys are server Url, values are token
            @JvmField
            val serverTokens: MutableMap<String, String> = mutableMapOf()
    )

    fun getToken(userId: String, serverUrl: String): String? {
        handleMigration(userId)

        return readStore()
                .userToServerTokens[userId]
                ?.serverTokens
                ?.get(serverUrl)
    }

    private fun handleMigration(userId: String) {
        val prefKey = SCALAR_TOKEN_LEGACY_PREFERENCE_KEY + userId

        val previousStoredToken = prefs.getString(prefKey, null)

        if (!previousStoredToken.isNullOrBlank()) {
            // It was maybe a token for scalar.vector.im. If it is not the case, it will be invalid and will be replaced.
            setToken(userId, "https://scalar.vector.im/api", previousStoredToken)

            prefs.edit {
                remove(prefKey)
            }
        }
    }

    fun setToken(userId: String, serverUrl: String, token: String) {
        readStore()
                .apply {
                    userToServerTokens.getOrPut(userId) { ServerTokens() }
                            .serverTokens[serverUrl] = token
                }
                .commit()
    }

    private fun readStore(): TokensStore {
        return prefs.getString(SCALAR_TOKENS_PREFERENCE_KEY, null)
                ?.toModel()
                ?: TokensStore()
    }

    private fun TokensStore.commit() {
        prefs.edit {
            putString(SCALAR_TOKENS_PREFERENCE_KEY, this@commit.fromModel())
        }
    }

    fun clear() {
        prefs.edit {
            remove(SCALAR_TOKENS_PREFERENCE_KEY)
        }
    }

    private fun String.toModel(): TokensStore? {
        return gson.fromJson<TokensStore>(this, TokensStore::class.java)
    }

    private fun TokensStore.fromModel(): String? {
        return gson.toJson(this)
    }

    companion object {
        private const val SCALAR_TOKEN_LEGACY_PREFERENCE_KEY = "SCALAR_TOKEN_PREFERENCE_KEY"

        private const val SCALAR_TOKENS_PREFERENCE_KEY = "SCALAR_TOKENS_PREFERENCE_KEY"
    }
}