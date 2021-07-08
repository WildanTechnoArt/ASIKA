package com.unpi.asika.presenter

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore
import com.unpi.asika.R
import com.unpi.asika.utils.Validation.Companion.validateEmail
import com.unpi.asika.utils.Validation.Companion.validateFields
import com.unpi.asika.view.LoginView

class LoginPresenter(private val context: Context,
                     private val view: LoginView.View
) : LoginView.Presenter {

    override fun requestLogin(email: String, password: String) {
        if (validateFields(email) || validateFields(password)) {
            view.handleResponse(context.getString(R.string.email_password_null))
        } else if (validateEmail(email)) {
            view.handleResponse(context.getString(R.string.email_not_valid))
        } else {
            view.showProgressBar()
            login(email, password)
        }
    }

    private fun login(email: String, password: String) {
        val mAuth = FirebaseAuth.getInstance()

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val userId = task.result?.user?.uid.toString()

                    val db = FirebaseFirestore.getInstance()
                        .collection("users")
                        .document(userId)
                        .get()

                    db.addOnSuccessListener { result ->
                        view.onSuccess(result)
                    }.addOnFailureListener {
                        view.hideProgressBar()
                        view.handleResponse(it.localizedMessage?.toString().toString())
                    }

                } else {
                    view.hideProgressBar()
                    view.handleResponse((task.exception as FirebaseAuthException).errorCode)
                }
            }
    }
}