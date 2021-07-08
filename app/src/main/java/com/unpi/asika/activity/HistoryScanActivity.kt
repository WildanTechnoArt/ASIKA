package com.unpi.asika.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.unpi.asika.R
import com.unpi.asika.adapter.AbsentAdapter
import com.unpi.asika.model.AbsentModel
import kotlinx.android.synthetic.main.activity_history_scan.*

class HistoryScanActivity : AppCompatActivity() {

    private var mUserId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_scan)
        prepare()
        checkAttendance()
    }

    private fun prepare() {
        swipe_refresh?.isRefreshing = true
        rv_absent.layoutManager = LinearLayoutManager(this)
        rv_absent.setHasFixedSize(true)
        mUserId = FirebaseAuth.getInstance().currentUser?.uid.toString()
        swipe_refresh?.setOnRefreshListener {
            checkAttendance()
        }
    }

    private fun loadAbsent() {
        val query = FirebaseFirestore.getInstance()
            .collection("users")
            .document(mUserId.toString())
            .collection("absensi")
            .orderBy("date")

        val options = FirestoreRecyclerOptions.Builder<AbsentModel>()
            .setQuery(query, AbsentModel::class.java)
            .setLifecycleOwner(this)
            .build()

        val adapter = AbsentAdapter(options)
        rv_absent?.adapter = adapter
    }

    private fun checkAttendance() {
        val db = FirebaseFirestore.getInstance()
            .collection("users")
            .document(mUserId.toString())
            .collection("absensi")

        db.addSnapshotListener { snapshot, _ ->
            if ((snapshot?.size() ?: 0) > 0) {
                rv_absent?.visibility = View.VISIBLE
                tv_no_absent?.visibility = View.GONE
                loadAbsent()
            } else {
                rv_absent?.visibility = View.GONE
                tv_no_absent?.visibility = View.VISIBLE
            }

            swipe_refresh?.isRefreshing = false
        }
    }
}