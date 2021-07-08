package com.unpi.asika.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.unpi.asika.GlideApp
import com.unpi.asika.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        loadImage()
        btnListener()
    }

    private fun loadImage() {
        GlideApp.with(this)
            .load(R.drawable.header)
            .into(img_header)

        GlideApp.with(this)
            .load(R.drawable.logo_asika)
            .into(img_asika)
    }

    private fun btnListener() {
        btn_login.setOnClickListener {
            startActivity(Intent(this, LoginFormActivity::class.java))
        }
        btn_register.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
}