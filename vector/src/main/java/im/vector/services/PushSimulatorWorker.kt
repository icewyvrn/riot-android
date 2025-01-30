/*
 * Copyright 2019-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */

package im.vector.services

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

/**
 * This class simulate push event when FCM is not working/disabled
 */
class PushSimulatorWorker(val context: Context,
                          workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        // Simulate a Push
        EventStreamServiceX.onSimulatedPushReceived(context)

        // Indicate whether the task finished successfully with the Result
        return Result.success()
    }
}