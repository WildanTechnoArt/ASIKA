package com.unpi.asika.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.unpi.asika.GlideApp
import com.unpi.asika.R
import com.unpi.asika.activity.DetailNewsActivity
import com.unpi.asika.model.NewsModel
import com.unpi.asika.utils.Constant.NEWS_CONTENT
import com.unpi.asika.utils.Constant.NEWS_DATE
import com.unpi.asika.utils.Constant.NEWS_IMAGE
import com.unpi.asika.utils.Constant.NEWS_TITLE
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.news_item.view.*

class NewsAdapter(
    options: FirestoreRecyclerOptions<NewsModel>
) :
    FirestoreRecyclerAdapter<NewsModel, NewsAdapter.ViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.news_item, parent, false)

        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int, item: NewsModel) {
        val context = holder.itemView.context
        val getImage = item.image.toString()
        val getTitle = item.title.toString()
        val getDateTime = item.datetime.toString()
        val getDescription = item.description.toString()

        holder.apply {
            GlideApp.with(context)
                .load(getImage)
                .placeholder(R.drawable.ic_image_100)
                .into(containerView.img_news)

            containerView.tv_title.text = getTitle
            containerView.tv_datetime.text = getDateTime
            containerView.tv_description.text = getDescription
            containerView.setOnClickListener {
                val intent = Intent(context, DetailNewsActivity::class.java)
                intent.putExtra(NEWS_IMAGE, getImage)
                intent.putExtra(NEWS_TITLE, getTitle)
                intent.putExtra(NEWS_DATE, getDateTime)
                intent.putExtra(NEWS_CONTENT, getDescription)
                (context as AppCompatActivity).startActivity(intent)
            }
        }
    }

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer
}