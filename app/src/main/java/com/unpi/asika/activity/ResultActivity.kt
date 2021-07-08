package com.unpi.asika.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.unpi.asika.R
import com.unpi.asika.utils.Constant
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {

    private var getDateTime: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        getDateTime = intent.getStringExtra(Constant.ABSENT_DATE).toString()
        tv_datetime.text = getDateTime

        btn_finish.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }
    }

    override fun onBackPressed() {
        //Dissable
    }
}