package com.unpi.asika.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.unpi.asika.GlideApp
import com.unpi.asika.R
import kotlinx.android.synthetic.main.fragment_todo_list.*

class TodoListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_todo_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepare()
    }

    private fun prepare() {
        GlideApp.with(this)
            .load(R.drawable.header)
            .into(img_header)
    }
}