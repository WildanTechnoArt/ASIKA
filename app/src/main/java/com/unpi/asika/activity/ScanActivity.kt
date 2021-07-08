package com.unpi.asika.activity

import android.Manifest
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import com.budiyev.android.codescanner.CodeScanner
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.unpi.asika.R
import com.unpi.asika.model.AbsentModel
import com.unpi.asika.utils.Constant.ABSENT_DATE
import com.unpi.asika.utils.Constant.USERNAME
import kotlinx.android.synthetic.main.activity_scan.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class ScanActivity : AppCompatActivity() {

    private lateinit var mCodeScanner: CodeScanner
    private val db = FirebaseFirestore.getInstance()
    private val db2 = FirebaseFirestore.getInstance()
    private val data = AbsentModel()
    private val mUserId = FirebaseAuth.getInstance().currentUser?.uid.toString()
    private var mUsername: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        init()
        checkCameraPermission()
    }

    override fun onResume() {
        super.onResume()
        checkCameraPermission()
    }

    override fun onPause() {
        mCodeScanner.releaseResources()
        super.onPause()
    }

    private fun init() {
        mUsername = intent.getStringExtra(USERNAME).toString()

        ivBgContent.bringToFront()
        mCodeScanner = CodeScanner(this, scannerView)
        mCodeScanner.setDecodeCallback {
            runOnUiThread {
                kotlin.run {
                    val code = it.text.toString()
                    checkCode(code)
                }
            }
        }
    }

    private fun checkCode(code: String) {
        progressBar.visibility = VISIBLE

        if (code == "http://en.m.wikipedia.org") {
            data.username = mUsername
            data.code = code
            data.date = Calendar.getInstance().time
            data.datetime = getDateTime()

            db.collection("users")
                .document(mUserId)
                .collection("absensi")
                .document()
                .set(data)
                .addOnSuccessListener {
                    changeStatus()
                }.addOnFailureListener {
                    progressBar.visibility = GONE
                    Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
                }
        } else {
            progressBar.visibility = VISIBLE
            checkCode()
        }
    }

    private fun checkCode() {
        val alert = this.let { data ->
            MaterialAlertDialogBuilder(data)
                .setMessage("QR Code tidak benar")
                .setPositiveButton("Kembali") { _, _ ->
                    finish()
                }
        }
        alert.create()
        alert.show()
    }

    private fun changeStatus() {
        val data = HashMap<String, Boolean>()
        data["checkIn"] = true

        db2.collection("absensi")
            .document(mUserId)
            .set(data)
            .addOnSuccessListener {
                progressBar.visibility = GONE
                val intent = Intent(this, ResultActivity::class.java)
                intent.putExtra(ABSENT_DATE, getDateTime())
                startActivity(intent)
                finish()
            }.addOnFailureListener {
                progressBar.visibility = GONE
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
            }
    }

    private fun getDateTime(): String {
        val c: Calendar = Calendar.getInstance()
        val df = SimpleDateFormat("EEE dd MMM, HH:mm:ss", Locale.getDefault())
        return df.format(c.time)
    }

    private fun checkCameraPermission() {
        Dexter.withActivity(this)
            .withPermission(Manifest.permission.CAMERA)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    mCodeScanner.startPreview()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    finish()
                }

            })
            .check()
    }
}