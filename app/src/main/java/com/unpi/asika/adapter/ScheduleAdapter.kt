package com.unpi.asika.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.unpi.asika.R
import com.unpi.asika.model.ScheduleModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.schedule_item.view.*

class ScheduleAdapter(
    options: FirestoreRecyclerOptions<ScheduleModel>
) :
    FirestoreRecyclerAdapter<ScheduleModel, ScheduleAdapter.ViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.schedule_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, item: ScheduleModel) {
        val context = holder.itemView.context
        val getDay = item.day.toString()
        val getWork = item.work
        val getClock = item.clock.toString()

        holder.apply {
            containerView.tv_day.text = getDay

            if (getWork == true) {
                containerView.tv_work.text = context.getString(R.string.come_to_work)
                containerView.tv_work.setTextColor(Color.parseColor("#388E3C"))
            } else {
                containerView.tv_work.text = context.getString(R.string.off_work)
                containerView.tv_office.text = "-"
                containerView.tv_work.setTextColor(Color.parseColor("#B00020"))
            }

            containerView.tv_clock.text = getClock
        }
    }

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer
}