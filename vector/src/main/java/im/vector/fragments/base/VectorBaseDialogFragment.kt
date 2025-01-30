/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */

package im.vector.fragments.base

import android.content.Context
import org.matrix.androidsdk.core.Log

/**
 * this class can be used as a parent class for DialogFragment to manager the listener
 */
abstract class VectorBaseDialogFragment<LISTENER> : androidx.fragment.app.DialogFragment() {

    protected var listener: LISTENER? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        // Cannot use (context is LISTENER)
        // for the moment, the listener is the Activity (and not the parent Fragment as it should)
        try {
            @Suppress("UNCHECKED_CAST")
            listener = context as LISTENER
        } catch (e: ClassCastException) {
            Log.w(LOG_TAG, "Parent Activity should implement the LISTENER interface")
        }
    }

    override fun onDetach() {
        super.onDetach()

        listener = null
    }

    companion object {
        private val LOG_TAG = VectorBaseDialogFragment::class.java.simpleName
    }
}