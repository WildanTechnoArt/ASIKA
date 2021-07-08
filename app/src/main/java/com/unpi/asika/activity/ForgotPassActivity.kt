package com.unpi.asika.activity

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.razir.progressbutton.attachTextChangeAnimator
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.unpi.asika.GlideApp
import com.unpi.asika.R
import com.unpi.asika.presenter.ForgotPasswordPresenter
import com.unpi.asika.view.ForgotPasswordView
import kotlinx.android.synthetic.main.activity_forgot_pass.*

class ForgotPassActivity : AppCompatActivity() , ForgotPasswordView.View {

    private lateinit var mEmail: String
    private lateinit var presenter: ForgotPasswordView.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pass)
        prepare()
        btn_reset.setOnClickListener {
            mEmail = input_email.text.toString()
            presenter.requestForgotPassword(mEmail)
        }
    }

    override fun onSuccess(message: String) {
        Toast.makeText(
            this, message,
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun handleResponse(message: String) {
        Toast.makeText(
            this, message,
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun showProgressBar() {
        btn_reset.showProgress { progressColor = Color.WHITE }
    }

    override fun hideProgressBar() {
        btn_reset.hideProgress(R.string.btn_reset)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun prepare() {
        GlideApp.with(this)
            .load(R.drawable.header)
            .into(img_header)

        presenter = ForgotPasswordPresenter(this, this)

        bindProgressButton(btn_reset)
        btn_reset.attachTextChangeAnimator()
    }
}