package com.unpi.asika.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.razir.progressbutton.attachTextChangeAnimator
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.firebase.firestore.DocumentSnapshot
import com.unpi.asika.GlideApp
import com.unpi.asika.R
import com.unpi.asika.presenter.LoginPresenter
import com.unpi.asika.view.LoginView
import kotlinx.android.synthetic.main.activity_login_form.*
import kotlinx.android.synthetic.main.activity_login_form.img_header
import kotlinx.android.synthetic.main.activity_login_form.input_email
import kotlinx.android.synthetic.main.activity_login_form.input_password

class LoginFormActivity : AppCompatActivity(), LoginView.View {

    private lateinit var presenter: LoginView.Presenter

    private lateinit var mEmail: String
    private lateinit var mPassword: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_form)
        init()
        loadImage()
        clickListener()
    }

    override fun onSuccess(result: DocumentSnapshot) {
        startActivity(Intent(this, MainActivity::class.java))
        finishAffinity()
    }

    override fun handleResponse(message: String) {
        when (message) {
            "ERROR_USER_NOT_FOUND" -> Toast.makeText(
                this, getString(R.string.error_user_not_found),
                Toast.LENGTH_SHORT
            ).show()

            "ERROR_WRONG_PASSWORD" -> Toast.makeText(
                this, getString(R.string.error_wrong_password),
                Toast.LENGTH_SHORT
            ).show()

            else -> Toast.makeText(
                this, message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun showProgressBar() {
        btn_login.showProgress { progressColor = Color.WHITE }
    }

    override fun hideProgressBar() {
        btn_login.hideProgress(R.string.btn_login)
    }

    private fun init() {
        presenter = LoginPresenter(this, this)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        tv_forgot_password.paintFlags = tv_forgot_password.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        bindProgressButton(btn_login)
        btn_login.attachTextChangeAnimator()
    }

    private fun clickListener() {
        tv_forgot_password.setOnClickListener {
            startActivity(Intent(this, ForgotPassActivity::class.java))
        }
        btn_login.setOnClickListener {
            mEmail = input_email.text.toString().trim()
            mPassword = input_password.text.toString().trim()
            presenter.requestLogin(mEmail, mPassword)
        }
        tv_register.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun loadImage() {
        GlideApp.with(this)
            .load(R.drawable.header_big)
            .fitCenter()
            .into(img_header)

        GlideApp.with(this)
            .load(R.drawable.logo_asika_white)
            .into(img_asika)
    }
}