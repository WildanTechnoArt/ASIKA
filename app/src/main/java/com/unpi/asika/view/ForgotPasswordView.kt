package com.unpi.asika.view

class ForgotPasswordView {

    interface View {
        fun onSuccess(message: String)
        fun handleResponse(message: String)
        fun showProgressBar()
        fun hideProgressBar()
    }

    interface Presenter {
        fun requestForgotPassword(email: String)
    }
}