package com.unpi.asika.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.unpi.asika.R
import kotlinx.android.synthetic.main.activity_verification.*

class VerificationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        tv_edit_email.paintFlags = tv_edit_email.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        btn_reset_pass.setOnClickListener {
            startActivity(Intent(this, NewPassActivity::class.java))
        }
        otp_view.setOtpCompletionListener {}
    }
}