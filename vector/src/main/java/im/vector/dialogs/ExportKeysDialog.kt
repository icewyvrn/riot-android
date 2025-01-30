/*
 * Copyright 2019-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package im.vector.dialogs

import android.app.Activity
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import im.vector.R

class ExportKeysDialog {

    fun show(activity: Activity, exportKeyDialogListener: ExportKeyDialogListener) {
        val dialogLayout = activity.layoutInflater.inflate(R.layout.dialog_export_e2e_keys, null)
        val builder = AlertDialog.Builder(activity)
                .setTitle(R.string.encryption_export_room_keys)
                .setView(dialogLayout)

        val passPhrase1EditText = dialogLayout.findViewById<TextInputEditText>(R.id.dialog_e2e_keys_passphrase_edit_text)
        val passPhrase2EditText = dialogLayout.findViewById<TextInputEditText>(R.id.dialog_e2e_keys_confirm_passphrase_edit_text)
        val passPhrase2Til = dialogLayout.findViewById<TextInputLayout>(R.id.dialog_e2e_keys_confirm_passphrase_til)
        val exportButton = dialogLayout.findViewById<Button>(R.id.dialog_e2e_keys_export_button)
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                when {
                    TextUtils.isEmpty(passPhrase1EditText.text)                          -> {
                        exportButton.isEnabled = false
                        passPhrase2Til.error = null
                    }
                    TextUtils.equals(passPhrase1EditText.text, passPhrase2EditText.text) -> {
                        exportButton.isEnabled = true
                        passPhrase2Til.error = null
                    }
                    else                                                                 -> {
                        exportButton.isEnabled = false
                        passPhrase2Til.error = activity.getString(R.string.passphrase_passphrase_does_not_match)
                    }
                }
            }
        }

        passPhrase1EditText.addTextChangedListener(textWatcher)
        passPhrase2EditText.addTextChangedListener(textWatcher)

        val exportDialog = builder.show()

        exportButton.setOnClickListener {
            exportKeyDialogListener.onPassphrase(passPhrase1EditText.text.toString())

            exportDialog.dismiss()
        }
    }


    interface ExportKeyDialogListener {
        fun onPassphrase(passphrase: String)
    }
}