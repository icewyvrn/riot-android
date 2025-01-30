/*
 * Copyright 2019-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */
package im.vector.fragments.terms

import android.view.View
import com.airbnb.epoxy.TypedEpoxyController
import im.vector.R
import im.vector.ui.epoxy.genericItemHeader

class TermsController(private val itemDescription: String,
                      private val listener: Listener) : TypedEpoxyController<List<Term>>() {

    override fun buildModels(data: List<Term>?) {
        data?.let {
            genericItemHeader {
                id("header")
                textID(R.string.widget_integration_review_terms)
            }
            it.forEach { term ->
                terms {
                    id(term.url)
                    name(term.name)
                    description(itemDescription)
                    checked(term.accepted)

                    clickListener(View.OnClickListener { listener.review(term) })
                    checkChangeListener { _, isChecked ->
                        listener.setChecked(term, isChecked)
                    }
                }
            }
        }
        //TODO error mgmt
    }

    interface Listener {
        fun setChecked(term: Term, isChecked: Boolean)
        fun review(term: Term)
    }
}