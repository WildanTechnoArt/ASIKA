package com.unpi.asika.activity

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
import com.unpi.asika.R
import com.unpi.asika.model.UserModel
import com.unpi.asika.utils.Validation.Companion.validateEmail
import com.unpi.asika.utils.Validation.Companion.validateFields
import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfileActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private var mUserId: String? = null
    private var mDefaultEmail: String? = null
    private var mUsername: String? = null
    private var mEmail: String? = null
    private var mPhone: String? = null
    private var mAddress: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        prepare()
    }

    private fun prepare() {
        mUserId = FirebaseAuth.getInstance().currentUser?.uid.toString()
        bindProgressButton(btn_edit_profile)
        btn_edit_profile.attachTextChangeAnimator()
        getProfile()
        btn_edit_profile.setOnClickListener {
            mUsername = input_name.text.toString()
            mUsername = input_email.text.toString()
            mUsername = input_name.text.toString()
            mUsername = input_name.text.toString()

            val user = UserModel()
            user.username = mUsername
            user.email = mEmail
            user.phone = mPhone
            user.address = mAddress

            if (validateFields(mUsername) || validateFields(mAddress) || validateFields(mPhone)
            ) {
                Toast.makeText(this, getString(R.string.warning_input_data), Toast.LENGTH_SHORT).show()
            } else if (validateEmail(mEmail.toString())) {
                Toast.makeText(this, getString(R.string.email_not_valid), Toast.LENGTH_SHORT).show()
            } else {
                if (mDefaultEmail == mEmail) {
                    editProfile(user)
                } else {
                    val mAuth = FirebaseAuth.getInstance().currentUser
                    mAuth?.updateEmail(mEmail.toString())
                        ?.addOnCompleteListener {
                            editProfile(user)
                        }
                        ?.addOnFailureListener {
                            Toast.makeText(this, it.localizedMessage?.toString().toString(), Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }
    }

    private fun getProfile() {
        db.collection("users")
            .document(mUserId.toString())
            .get()
            .addOnSuccessListener {
                btn_edit_profile.isEnabled = true

                val getName = it?.getString("username").toString()
                mDefaultEmail = it.getString("email").toString()
                val getAddress = it?.getString("address").toString()
                val getPhone = it?.getString("phone").toString()

                input_name.setText(getName)
                input_email.setText(mDefaultEmail)
                input_address.setText(getAddress)
                input_phone.setText(getPhone)
            }.addOnFailureListener {
                Toast.makeText(this, it.localizedMessage?.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    private fun editProfile(student: UserModel) {
        btn_edit_profile.showProgress { progressColor = Color.WHITE }

        val db = FirebaseFirestore.getInstance()
            .collection("users").document(mUserId.toString())

        db.set(student)
            .addOnSuccessListener {
                Toast.makeText(this, getString(R.string.success_edit_profile), Toast.LENGTH_SHORT).show()
                finish()
            }.addOnFailureListener {
                btn_edit_profile.hideProgress(R.string.btn_edit_profile)
                Toast.makeText(this, it.localizedMessage?.toString(), Toast.LENGTH_SHORT).show()
            }
    }
}