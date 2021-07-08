package com.unpi.asika.fcm

import android.content.Context
import android.content.Intent
import com.google.firebase.firestore.FirebaseFirestore
import com.onesignal.OSNotificationOpenedResult
import com.onesignal.OneSignal
import com.unpi.asika.activity.SplashScreenActivity
import com.unpi.asika.model.NotificationModel
import java.text.SimpleDateFormat
import java.util.*

class NotificationOpenedHandler(private var ctx: Context) :
    OneSignal.OSNotificationOpenedHandler {

    override fun notificationOpened(result: OSNotificationOpenedResult?) {
        val data = result?.notification?.additionalData

        val title: String?
        val message: String?

        if (data != null) {
            title = data.getString("title")
            message = data.getString("message")

            val notify = NotificationModel()
            notify.title = title
            notify.message = message
            notify.datetime = getDateTime()
            notify.date = Calendar.getInstance().time

            val db = FirebaseFirestore.getInstance()
            db.collection("notification")
                .document()
                .set(notify)
                .addOnSuccessListener {
                    val intent = Intent(ctx, SplashScreenActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NEW_TASK
                    ctx.startActivity(intent)
                }
        }
    }

    private fun getDateTime(): String {
        val c: Calendar = Calendar.getInstance()
        val df = SimpleDateFormat("EEE dd MMM, HH:mm:ss", Locale.getDefault())
        return df.format(c.time)
    }
}