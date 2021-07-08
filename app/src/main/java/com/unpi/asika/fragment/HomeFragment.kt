package com.unpi.asika.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.unpi.asika.GlideApp
import com.unpi.asika.R
import com.unpi.asika.activity.*
import com.unpi.asika.adapter.AbsentAdapter
import com.unpi.asika.adapter.NewsAdapter
import com.unpi.asika.model.AbsentModel
import com.unpi.asika.model.NewsModel
import com.unpi.asika.utils.Constant.USERNAME
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.header_home.*
import kotlinx.android.synthetic.main.main_menu_layout.*
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private var mUserId: String? = null
    private val dbPhoto = FirebaseFirestore.getInstance()
    private val dbProfile = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepare()
        requestData()
        showDateTime()
        loadNews()
        loadAbsent()
        onClickListener()
        checkAttendance()
        attendanceCount()
        swipe_refresh?.setOnRefreshListener {
            showDateTime()
            loadAbsent()
            requestData()
            loadNews()
            checkAttendance()
            attendanceCount()
        }
    }

    private fun prepare() {
        rv_news.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rv_absent.layoutManager = LinearLayoutManager(context)
    }

    private fun onClickListener() {
        card_history.setOnClickListener {
            startActivity(Intent(context, HistoryScanActivity::class.java))
        }
        card_submission.setOnClickListener {}
        card_schedule.setOnClickListener {
            startActivity(Intent(context, ScheduleActivity::class.java))
        }
        card_reimbursement.setOnClickListener {}
        card_report.setOnClickListener {
            startActivity(Intent(context, SalaryReportActivity::class.java))
        }
        img_notification.setOnClickListener {
            startActivity(Intent(context, NotificationActivity::class.java))
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

    private fun requestData() {
        mUserId = FirebaseAuth.getInstance().currentUser?.uid.toString()
        swipe_refresh?.isRefreshing = true
        showPhoto()
        showProfile()
    }

    private fun showDateTime() {
        val c: Calendar = Calendar.getInstance()
        val df = SimpleDateFormat("EEE, dd MMM yyyy - ", Locale.getDefault())
        val formattedDate: String = df.format(c.time)
        tv_datetime.text = formattedDate
    }

    private fun showPhoto() {
        dbPhoto.collection("photos")
            .document(mUserId.toString())
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Toast.makeText(context, error.localizedMessage, Toast.LENGTH_SHORT).show()
                } else {
                    val getPhoto = value?.getString("photoUrl").toString()
                    context?.let {
                        GlideApp.with(it)
                            .load(getPhoto)
                            .placeholder(R.drawable.icon_profile)
                            .into(img_profile)
                    }
                }

                swipe_refresh?.isRefreshing = false
            }
    }

    private fun showProfile() {
        dbProfile.collection("users")
            .document(mUserId.toString())
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Toast.makeText(context, error.localizedMessage, Toast.LENGTH_SHORT).show()
                } else {
                    val getName = value?.getString("username").toString()
                    tv_username.text = getName
                }

                swipe_refresh?.isRefreshing = false
            }
    }

    private fun checkAttendance() {
        val db = FirebaseFirestore.getInstance()
        db.collection("absensi")
            .document(mUserId.toString())
            .addSnapshotListener { snapshot, _ ->
                val getStatus = snapshot?.getBoolean("checkIn")
                if (getStatus == true && snapshot.exists()) {
                    card_scanner?.setCardBackgroundColor(Color.parseColor("#A3A3A3"))
                    card_scanner?.setOnClickListener {
                        Toast.makeText(context, "Anda sudah melakukan absensi", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    card_scanner?.setCardBackgroundColor(Color.parseColor("#00BD9D"))
                    card_scanner?.setOnClickListener {
                        val intent = Intent(context, ScanActivity::class.java)
                        intent.putExtra(USERNAME, tv_username.text.toString())
                        startActivity(intent)
                    }
                }
            }
    }

    private fun attendanceCount() {
        val db = FirebaseFirestore.getInstance()
            .collection("users")
            .document(mUserId.toString())
            .collection("absensi")

        db.addSnapshotListener { snapshot, _ ->
            tv_work.text = snapshot?.size().toString()
        }
    }
}