package com.unpi.asika.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.unpi.asika.R
import com.unpi.asika.model.NotificationModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.notification_item.view.*

class NotificationAdapter(
    options: FirestoreRecyclerOptions<NotificationModel>
) :
    FirestoreRecyclerAdapter<NotificationModel, NotificationAdapter.ViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.notification_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, item: NotificationModel) {
        val getTitle = item.title.toString()
        val getMessage = item.message.toString()
        val getDatetime = item.datetime.toString()

        holder.apply {
            containerView.tv_title.text = getTitle
            containerView.tv_datetime.text = getDatetime
            containerView.tv_message.text = getMessage
        }
    }

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer
}