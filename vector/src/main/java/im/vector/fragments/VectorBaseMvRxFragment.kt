/*
 * Copyright 2019-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */
package im.vector.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import butterknife.ButterKnife
import butterknife.Unbinder
import com.airbnb.mvrx.BaseMvRxFragment
import im.vector.activity.VectorAppCompatActivity
import org.matrix.androidsdk.core.Log


abstract class VectorBaseMvRxFragment : BaseMvRxFragment() {
    // Butterknife unbinder
    private var mUnBinder: Unbinder? = null

    protected var vectorActivity: VectorAppCompatActivity? = null

    /* ==========================================================================================
     * Life cycle
     * ========================================================================================== */

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (getMenuRes() != -1) {
            setHasOptionsMenu(true)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutResId(), container, false)
    }

    @LayoutRes
    abstract fun getLayoutResId(): Int

    @CallSuper
    override fun onResume() {
        super.onResume()

        Log.event(Log.EventTag.NAVIGATION, "onResume Fragment " + this.javaClass.simpleName)
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mUnBinder = ButterKnife.bind(this, view)
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        mUnBinder?.unbind()
        mUnBinder = null
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        vectorActivity = context as VectorAppCompatActivity
    }

    override fun onDetach() {
        super.onDetach()

        vectorActivity = null
    }

    /* ==========================================================================================
     * MENU MANAGEMENT
     * ========================================================================================== */

    open fun getMenuRes() = -1

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val menuRes = getMenuRes()

        if (menuRes != -1) {
            inflater.inflate(menuRes, menu)
        }
    }
}