package com.unpi.asika.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.unpi.asika.R
import com.unpi.asika.model.AbsentModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.absent_item.view.*

class AbsentAdapter(
    options: FirestoreRecyclerOptions<AbsentModel>
) :
    FirestoreRecyclerAdapter<AbsentModel, AbsentAdapter.ViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.absent_item, parent, false)

        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int, item: AbsentModel) {
        val getUsername = item.username.toString()
        val getDatetime = item.datetime.toString()

        holder.apply {
            containerView.tv_username.text = getUsername
            containerView.tv_datetime.text = getDatetime
        }
    }

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer
}