package com.unpi.asika.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.razir.progressbutton.attachTextChangeAnimator
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.unpi.asika.GlideApp
import com.unpi.asika.R
import com.unpi.asika.model.UserModel
import com.unpi.asika.utils.Validation.Companion.validateEmail
import com.unpi.asika.utils.Validation.Companion.validateFields
import kotlinx.android.synthetic.main.activity_login.img_header
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    private var mUsername: String? = null
    private var mEmail: String? = null
    private var mPassword: String? = null
    private var mRePassword: String? = null
    private var mAddress: String? = null
    private var mPhone: String? = null

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        prepare()
    }

    private fun prepare() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        bindProgressButton(btn_signup)
        btn_signup.attachTextChangeAnimator()

        mAuth = FirebaseAuth.getInstance()

        loadImage()
        btn_signup.setOnClickListener {
            createAccount()
        }
    }

    private fun createAccount() {
        mUsername = input_name.text.toString()
        mEmail = input_email.text.toString()
        mPassword = input_password.text.toString()
        mRePassword = input_retype_password.text.toString()
        mAddress = input_address.text.toString()
        mPhone = input_phone.text.toString()

        if (validateFields(mUsername) || validateFields(mPhone) ||
            validateFields(mPassword) || validateFields(mAddress)
        ) {

            Toast.makeText(
                this, getString(R.string.warning_input_data),
                Toast.LENGTH_SHORT
            ).show()

        } else if (validateEmail(mEmail.toString())) {

            Toast.makeText(
                this, getString(R.string.email_not_valid),
                Toast.LENGTH_SHORT
            ).show()

        } else {
            if (mPassword == mRePassword) {
                btn_signup.showProgress { progressColor = Color.WHITE }

                mAuth.createUserWithEmailAndPassword(mEmail.toString(), mPassword.toString())
                    .addOnSuccessListener {
                        it.user?.uid?.let { it1 -> addDataUser(it1) }
                    }.addOnFailureListener {
                        btn_signup.hideProgress(R.string.btn_signup)
                        Toast.makeText(
                            this, it.localizedMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } else {
                Toast.makeText(
                    this, getString(R.string.password_not_valid),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun addDataUser(userId: String) {
        val model = UserModel()
        model.username = mUsername
        model.email = mEmail
        model.address = mAddress
        model.phone = mPhone

        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(userId)
            .set(model)
            .addOnSuccessListener {
                startActivity(Intent(this, MainActivity::class.java))
                finishAffinity()
            }.addOnFailureListener {
                btn_signup.hideProgress(R.string.btn_signup)

                Toast.makeText(
                    this, it.localizedMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun loadImage() {
        GlideApp.with(this)
            .load(R.drawable.header)
            .into(img_header)
    }

    /*
    private fun showDialog() {
        var alertDialog: AlertDialog? = null

        val builder = MaterialAlertDialogBuilder(this)
        val dialogView = layoutInflater.inflate(R.layout.varification_dialog, null)

        dialogView.btn_next.setOnClickListener {
            alertDialog?.dismiss()
            startActivity(Intent(this, VerificationEmailActivity::class.java))
        }

        builder.setView(dialogView)
        builder.setCancelable(false)
        alertDialog = builder.create()
        alertDialog.show()
    }
     */
}