package com.unpi.asika.activity

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.unpi.asika.R
import kotlinx.android.synthetic.main.activity_verification_email.*

class VerificationEmailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification_email)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        btn_verification.setOnClickListener {
            Toast.makeText(this, "Verifikasi Email", Toast.LENGTH_SHORT).show()
        }
    }
}