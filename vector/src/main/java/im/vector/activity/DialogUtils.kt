/*
 * Copyright 2019-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */

package im.vector.activity

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import im.vector.R
import im.vector.extensions.showPassword

object DialogUtils {

    fun promptPassword(context: Context, errorText: String? = null, defaultPwd: String? = null,
                       done: (String) -> Unit,
                       cancel: (() -> Unit)? = null) {
        val view: ViewGroup = LayoutInflater.from(context).inflate(R.layout.dialog_confirm_password, null) as ViewGroup

        val showPassword: ImageView = view.findViewById(R.id.confirm_password_show_passwords)
        val passwordTil: TextInputLayout = view.findViewById(R.id.confirm_password_til)
        val passwordText: TextInputEditText = view.findViewById(R.id.password_label)
        passwordText.setText(defaultPwd)

        var passwordShown = false

        showPassword.setOnClickListener {
            passwordShown = !passwordShown
            passwordText.showPassword(passwordShown)
            showPassword.setImageResource(if (passwordShown) R.drawable.ic_eye_closed_black else R.drawable.ic_eye_black)
        }

        passwordTil.error = errorText

        AlertDialog.Builder(context)
                .setView(view)
                .setPositiveButton(R.string._continue) { tv, _ ->
                    done(passwordText.text.toString())
                }
                .apply {
                    if (cancel != null) {
                        setNegativeButton(R.string.cancel) { _, _ ->
                            cancel()
                        }
                    }
                }

                .show()

    }
}