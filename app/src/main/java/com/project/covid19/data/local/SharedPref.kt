package com.project.covid19.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import javax.inject.Inject

class SharedPref @Inject constructor(): ISharedPref {

    companion object {
        private var pref: SharedPreferences?= null
        @Volatile
        private var instance: SharedPref?= null
        private var LOCK = Any()

        operator fun invoke(context: Context): SharedPref = instance ?: synchronized(LOCK) {
            this.instance ?: buildSharedPref(context).also {
                this.instance = it
            }
        }
        private fun buildSharedPref(context: Context): SharedPref {
            this.pref = PreferenceManager.getDefaultSharedPreferences(context)
            return SharedPref()
        }
    }
}