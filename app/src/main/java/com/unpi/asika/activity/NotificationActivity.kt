package com.unpi.asika.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.unpi.asika.R
import com.unpi.asika.adapter.NotificationAdapter
import com.unpi.asika.model.NotificationModel
import kotlinx.android.synthetic.main.activity_notification.*

class NotificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        prepare()
        checkNotification()
    }

    private fun prepare() {
        swipe_refresh?.isRefreshing = true
        rv_notification.layoutManager = LinearLayoutManager(this)
        rv_notification.setHasFixedSize(true)
        swipe_refresh?.setOnRefreshListener {
            checkNotification()
        }
    }

    private fun loadNotification() {
        val query = FirebaseFirestore.getInstance()
            .collection("notification")
            .orderBy("date")

        val options = FirestoreRecyclerOptions.Builder<NotificationModel>()
            .setQuery(query, NotificationModel::class.java)
            .setLifecycleOwner(this)
            .build()

        val adapter = NotificationAdapter(options)
        rv_notification?.adapter = adapter
    }

    private fun checkNotification() {
        val db = FirebaseFirestore.getInstance()
            .collection("notification")

        db.addSnapshotListener { snapshot, _ ->
            if ((snapshot?.size() ?: 0) > 0) {
                rv_notification?.visibility = View.VISIBLE
                tv_no_notification?.visibility = View.GONE
                loadNotification()
            } else {
                rv_notification?.visibility = View.GONE
                tv_no_notification?.visibility = View.VISIBLE
            }

            swipe_refresh?.isRefreshing = false
        }
    }
}