package com.unpi.asika.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.unpi.asika.GlideApp
import com.unpi.asika.R
import com.unpi.asika.adapter.NewsAdapter
import com.unpi.asika.model.NewsModel
import kotlinx.android.synthetic.main.fragment_news.*

class NewsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepare()
        checkNews()
    }

    private fun prepare() {
        swipe_refresh?.isRefreshing = true

        rv_news.layoutManager = LinearLayoutManager(context)
        rv_news.setHasFixedSize(true)

        GlideApp.with(this)
            .load(R.drawable.header)
            .into(img_header)

        swipe_refresh?.setOnRefreshListener {
            checkNews()
        }
    }

    private fun loadNews() {
        val query = FirebaseFirestore.getInstance()
            .collection("news")

        val options = FirestoreRecyclerOptions.Builder<NewsModel>()
            .setQuery(query, NewsModel::class.java)
            .setLifecycleOwner(this)
            .build()

        val adapter = NewsAdapter(options)
        rv_news?.adapter = adapter
    }

    private fun checkNews() {
        val db = FirebaseFirestore.getInstance()
            .collection("news")

        db.addSnapshotListener { snapshot, _ ->
            if ((snapshot?.size() ?: 0) > 0) {
                rv_news?.visibility = View.VISIBLE
                tv_no_news?.visibility = View.GONE
                loadNews()
            } else {
                rv_news?.visibility = View.GONE
                tv_no_news?.visibility = View.VISIBLE
            }

            swipe_refresh?.isRefreshing = false
        }
    }
}