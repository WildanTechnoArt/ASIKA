package com.unpi.asika.activity

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.unpi.asika.R
import kotlinx.android.synthetic.main.activity_new_pass.*

class NewPassActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_pass)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        btn_save_pass.setOnClickListener {
            Toast.makeText(this, "Simpan Password Baru", Toast.LENGTH_SHORT).show()
        }
    }
}