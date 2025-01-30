/*
 * Copyright 2019-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */
package im.vector.ui.animation

import android.view.Gravity
import android.view.View
import androidx.transition.ChangeBounds
import androidx.transition.Fade
import androidx.transition.Slide
import androidx.transition.TransitionSet

class VectorTransitionSet : TransitionSet() {

    init {
        // Change bounds for every Views
        addTransition(ChangeBounds())
    }

    private val slideStart by lazy { Slide(Gravity.START).apply { addTransition(this) } }

    private val sliderEnd by lazy { Slide(Gravity.END).apply { addTransition(this) } }

    private val slideBottom by lazy { Slide(Gravity.BOTTOM).apply { addTransition(this) } }

    private val slideTop by lazy { Slide(Gravity.TOP).apply { addTransition(this) } }

    private val alpha by lazy { Fade().apply { addTransition(this) } }

    fun appearFromTop(view: View) {
        slideTop.addTarget(view)
    }

    fun appearFromBottom(view: View) {
        slideBottom.addTarget(view)
    }

    fun appearFromStart(view: View) {
        slideStart.addTarget(view)
    }

    fun appearFromEnd(view: View) {
        sliderEnd.addTarget(view)
    }

    fun appearWithAlpha(view: View) {
        alpha.addTarget(view)
    }
}