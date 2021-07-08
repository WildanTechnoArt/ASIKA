package com.unpi.asika.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.unpi.asika.R
import com.unpi.asika.adapter.ReportAdapter
import com.unpi.asika.model.ReportModel
import kotlinx.android.synthetic.main.activity_salary_report.*

class SalaryReportActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_salary_report)
        prepare()
        checkSalaryReport()
    }

    private fun prepare() {
        swipe_refresh?.isRefreshing = true
        rv_report.layoutManager = LinearLayoutManager(this)
        rv_report.setHasFixedSize(true)
        swipe_refresh?.setOnRefreshListener {
            checkSalaryReport()
        }
    }

    private fun loadSalaryReport() {
        val query = FirebaseFirestore.getInstance()
            .collection("salaryReport")
            .orderBy("position")

        val options = FirestoreRecyclerOptions.Builder<ReportModel>()
            .setQuery(query, ReportModel::class.java)
            .setLifecycleOwner(this)
            .build()

        val adapter = ReportAdapter(options)
        rv_report?.adapter = adapter
    }

    private fun checkSalaryReport() {
        val db = FirebaseFirestore.getInstance()
            .collection("salaryReport")

        db.addSnapshotListener { snapshot, _ ->
            if ((snapshot?.size() ?: 0) > 0) {
                rv_report?.visibility = View.VISIBLE
                tv_no_report?.visibility = View.GONE
                loadSalaryReport()
            } else {
                rv_report?.visibility = View.GONE
                tv_no_report?.visibility = View.VISIBLE
            }

            swipe_refresh?.isRefreshing = false
        }
    }
}