package com.unpi.asika.utils

import android.text.TextUtils
import android.util.Patterns

class Validation {

    companion object {
        fun validateFields(input: String?): Boolean {
            return TextUtils.isEmpty(input)
        }

        fun validateEmail(string: CharSequence): Boolean {
            return (TextUtils.isEmpty(string) || !Patterns.EMAIL_ADDRESS.matcher(string).matches())
        }
    }
}