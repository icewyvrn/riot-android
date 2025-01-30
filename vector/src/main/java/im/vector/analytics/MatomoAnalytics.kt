/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package im.vector.analytics

import android.content.Context
import im.vector.R
import org.matomo.sdk.Matomo
import org.matomo.sdk.QueryParams
import org.matomo.sdk.Tracker
import org.matomo.sdk.TrackerBuilder
import org.matomo.sdk.extra.CustomVariables
import org.matomo.sdk.extra.TrackHelper

/**
 * A class implementing the Analytics interface for the Matomo solution
 */
class MatomoAnalytics(context: Context) : Analytics {
    private val tracker: Tracker

    init {
        val builder = TrackerBuilder(context.getString(R.string.matomo_server_url),
                context.getString(R.string.matomo_site_id).toInt(),
                context.getString(R.string.matomo_tracker_name))
        tracker = builder.build(Matomo.getInstance(context))
    }

    override fun trackScreen(screen: String, title: String?) {
        TrackHelper.track()
                .screen(screen)
                .title(title)
                .with(tracker)
    }

    override fun trackEvent(event: TrackingEvent) {
        TrackHelper.track()
                .event(event.category.value, event.action.value)
                .name(event.title)
                .value(event.value)
                .with(tracker)
    }

    @Suppress("DEPRECATION")
    override fun visitVariable(index: Int, name: String, value: String) {
        val customVariables = CustomVariables(tracker.defaultTrackMe.get(QueryParams.VISIT_SCOPE_CUSTOM_VARIABLES))
        customVariables.put(index, name, value)
        tracker.defaultTrackMe.set(QueryParams.VISIT_SCOPE_CUSTOM_VARIABLES, customVariables.toString())
    }

    override fun forceDispatch() {
        tracker.dispatch()
    }
}
