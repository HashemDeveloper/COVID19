package com.project.covid19.utils

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.FragmentActivity

class Constants {
    companion object {
        fun hideKeyboard(activity: FragmentActivity?) {
            val imm: InputMethodManager? = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm?.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
        }
    }
}