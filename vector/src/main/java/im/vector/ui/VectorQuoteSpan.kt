/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */

package im.vector.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.Layout
import android.text.style.LeadingMarginSpan
import android.text.style.LineBackgroundSpan
import androidx.core.content.ContextCompat
import im.vector.R

/**
 * android.text.style.QuoteSpan hard-codes the strip color and gap.
 * This class allow to customize color and size of the QuoteSpan
 *
 * Inspired from https://medium.com/@459631839/style-blockquotes-in-android-textviews-de8656057c3d
 */
class VectorQuoteSpan(context: Context) : LeadingMarginSpan, LineBackgroundSpan {

    private val backgroundColor = ContextCompat.getColor(context, R.color.quote_background_color)
    private val stripeColor = ContextCompat.getColor(context, R.color.quote_strip_color)
    private val stripeWidth = context.resources.getDimension(R.dimen.quote_width)
    private val gap = context.resources.getDimension(R.dimen.quote_gap)

    override fun getLeadingMargin(first: Boolean) = (stripeWidth + gap).toInt()

    override fun drawLeadingMargin(c: Canvas,
                                   p: Paint,
                                   x: Int,
                                   dir: Int,
                                   top: Int,
                                   baseline: Int,
                                   bottom: Int,
                                   text: CharSequence,
                                   start: Int,
                                   end: Int,
                                   first: Boolean,
                                   layout: Layout) {
        val style = p.style
        val paintColor = p.color

        p.style = Paint.Style.FILL
        p.color = stripeColor

        c.drawRect(x.toFloat(), top.toFloat(), x + dir * stripeWidth, bottom.toFloat(), p)

        p.style = style
        p.color = paintColor
    }

    override fun drawBackground(c: Canvas,
                                p: Paint,
                                left: Int,
                                right: Int,
                                top: Int,
                                baseline: Int,
                                bottom: Int,
                                text: CharSequence,
                                start: Int,
                                end: Int,
                                lnum: Int) {
        val paintColor = p.color
        p.color = backgroundColor
        c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), p)
        p.color = paintColor
    }
}