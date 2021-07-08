package com.unpi.asika.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.unpi.asika.R
import com.unpi.asika.model.ReportModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.report_item.view.*

class ReportAdapter(
    options: FirestoreRecyclerOptions<ReportModel>
) :
    FirestoreRecyclerAdapter<ReportModel, ReportAdapter.ViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.report_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, item: ReportModel) {
        val getDate = item.date.toString()
        val getSalary = item.salary
        val getNote = item.note.toString()
        val getPercent = item.percent.toString()
        val getCurrency = item.currency.toString()

        holder.apply {
            containerView.tv_date.text = getDate
            containerView.tv_percent.text = getPercent
            containerView.tv_note.text = getNote
            containerView.tv_salary.text = getCurrency

            if (getSalary == true) {
                containerView.tv_percent.setTextColor(Color.parseColor("#388E3C"))
            } else {
                containerView.tv_percent.setTextColor(Color.parseColor("#B00020"))
            }
        }
    }

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer
}