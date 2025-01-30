/*
 * Copyright 2018-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 * Please see LICENSE in the repository root for full details.
 */
package im.vector.fragments.troubleshoot

import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import kotlin.properties.Delegates

class NotificationTroubleshootTestManager(val fragment: Fragment) {

    val testList = ArrayList<TroubleshootTest>()
    var isCancelled = false

    var currentTestIndex by Delegates.observable(0) { _, _, _ ->
        statusListener?.invoke(this)
    }
    val adapter = NotificationTroubleshootRecyclerViewAdapter(testList)


    var statusListener: ((NotificationTroubleshootTestManager) -> Unit)? = null

    var diagStatus: TroubleshootTest.TestStatus by Delegates.observable(TroubleshootTest.TestStatus.NOT_STARTED) { _, _, _ ->
        statusListener?.invoke(this)
    }


    fun addTest(test: TroubleshootTest) {
        testList.add(test)
        test.manager = this
    }

    fun runDiagnostic() {
        if (isCancelled) return
        currentTestIndex = 0
        val handler = Handler(Looper.getMainLooper())
        diagStatus = if (testList.size > 0) TroubleshootTest.TestStatus.RUNNING else TroubleshootTest.TestStatus.SUCCESS
        var isAllGood = true
        for ((index, test) in testList.withIndex()) {
            test.statusListener = {
                if (!isCancelled) {
                    adapter.notifyItemChanged(index)
                    if (it.isFinished()) {
                        isAllGood = isAllGood && (it.status == TroubleshootTest.TestStatus.SUCCESS)
                        currentTestIndex++
                        if (currentTestIndex < testList.size) {
                            val troubleshootTest = testList[currentTestIndex]
                            troubleshootTest.status = TroubleshootTest.TestStatus.RUNNING
                            //Cosmetic: Start with a small delay for UI/UX reason (better animation effect) for non async tests
                            handler.postDelayed({
                                if (fragment.isAdded) {
                                    troubleshootTest.perform()
                                }
                            }, 600)
                        } else {
                            //we are done, test global status?
                            diagStatus = if (isAllGood) TroubleshootTest.TestStatus.SUCCESS else TroubleshootTest.TestStatus.FAILED
                        }
                    }
                }
            }
        }
        if (fragment.isAdded) {
            testList.firstOrNull()?.perform()
        }
    }

    fun retry() {
        for (test in testList) {
            test.cancel()
            test.description = null
            test.quickFix = null
            test.status = TroubleshootTest.TestStatus.NOT_STARTED
        }
        runDiagnostic()
    }

    fun cancel() {
        isCancelled = true
        for (test in testList) {
            test.cancel()
        }
    }

    companion object {
        const val REQ_CODE_FIX = 9099
    }
}