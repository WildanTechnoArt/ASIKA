package com.unpi.asika.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.unpi.asika.R
import com.unpi.asika.adapter.ScheduleAdapter
import com.unpi.asika.model.ScheduleModel
import kotlinx.android.synthetic.main.activity_schedule.*

class ScheduleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)
        prepare()
        checkSchedule()
    }

    private fun prepare() {
        swipe_refresh?.isRefreshing = true
        rv_schedule.layoutManager = LinearLayoutManager(this)
        rv_schedule.setHasFixedSize(true)
        swipe_refresh?.setOnRefreshListener {
            checkSchedule()
        }
    }

    private fun loadSchedule() {
        val query = FirebaseFirestore.getInstance()
            .collection("schedule")
            .orderBy("position")

        val options = FirestoreRecyclerOptions.Builder<ScheduleModel>()
            .setQuery(query, ScheduleModel::class.java)
            .setLifecycleOwner(this)
            .build()

        val adapter = ScheduleAdapter(options)
        rv_schedule?.adapter = adapter
    }

    private fun checkSchedule() {
        val db = FirebaseFirestore.getInstance()
            .collection("schedule")

        db.addSnapshotListener { snapshot, _ ->
            if ((snapshot?.size() ?: 0) > 0) {
                rv_schedule?.visibility = View.VISIBLE
                tv_no_schedule?.visibility = View.GONE
                loadSchedule()
            } else {
                rv_schedule?.visibility = View.GONE
                tv_no_schedule?.visibility = View.VISIBLE
            }

            swipe_refresh?.isRefreshing = false
        }
    }
}