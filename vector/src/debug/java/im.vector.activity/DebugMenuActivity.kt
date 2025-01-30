/*
 * Copyright 2019-2025 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package im.vector.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import butterknife.OnClick
import im.vector.R


class DebugMenuActivity : VectorAppCompatActivity() {

    override fun getLayoutRes() = R.layout.activity_debug_menu

    @OnClick(R.id.debug_test_text_view_link)
    fun testTextViewLink() {
        startActivity(Intent(this, TestLinkifyActivity::class.java))
    }

    @OnClick(R.id.debug_test_notification)
    fun testNotification() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create channel first
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                    NotificationChannel(
                            "CHAN",
                            "Channel name",
                            NotificationManager.IMPORTANCE_DEFAULT
                    )

            channel.description = "Channel description"
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel)

            val channel2 =
                    NotificationChannel(
                            "CHAN2",
                            "Channel name 2",
                            NotificationManager.IMPORTANCE_DEFAULT
                    )

            channel2.description = "Channel description 2"
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel2)
        }


        val builder = NotificationCompat.Builder(this, "CHAN")
                .setWhen(System.currentTimeMillis())
                .setContentTitle("Title")
                .setContentText("Content")
                // No effect because it's a group summary notif
                .setNumber(33)
                .setSmallIcon(R.drawable.logo_transparent)
                // This provocate the badge issue: no badge for group notification
                .setGroup("GroupKey")
                .setGroupSummary(true)

        val messagingStyle1 = NotificationCompat.MessagingStyle(
                Person.Builder()
                        .setName("User name")
                        .build()
        )
                .addMessage("Message 1 - 1", System.currentTimeMillis(), Person.Builder().setName("user 1-1").build())
                .addMessage("Message 1 - 2", System.currentTimeMillis(), Person.Builder().setName("user 1-2").build())

        val messagingStyle2 = NotificationCompat.MessagingStyle(
                Person.Builder()
                        .setName("User name 2")
                        .build()
        )
                .addMessage("Message 2 - 1", System.currentTimeMillis(), Person.Builder().setName("user 1-1").build())
                .addMessage("Message 2 - 2", System.currentTimeMillis(), Person.Builder().setName("user 1-2").build())


        notificationManager.notify(10, builder.build())

        notificationManager.notify(
                11,
                NotificationCompat.Builder(this, "CHAN")
                        .setChannelId("CHAN")
                        .setWhen(System.currentTimeMillis())
                        .setContentTitle("Title 1")
                        .setContentText("Content 1")
                        // For shortcut on long press on launcher icon
                        .setBadgeIconType(NotificationCompat.BADGE_ICON_NONE)
                        .setStyle(messagingStyle1)
                        .setSmallIcon(R.drawable.logo_transparent)
                        .setGroup("GroupKey")
                        .build()
        )

        notificationManager.notify(
                12,
                NotificationCompat.Builder(this, "CHAN2")
                        .setWhen(System.currentTimeMillis())
                        .setContentTitle("Title 2")
                        .setContentText("Content 2")
                        .setStyle(messagingStyle2)
                        .setSmallIcon(R.drawable.logo_transparent)
                        .setGroup("GroupKey")
                        .build()
        )
    }
}

