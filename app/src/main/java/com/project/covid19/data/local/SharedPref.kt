package com.project.covid19.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.project.covid19.utils.Constants
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

    override fun setIsNightModeOn(isNightModeOn: Boolean) {
        pref?.edit(commit = true) {
            putBoolean(Constants.IS_NIGHT_MODE, isNightModeOn)
        }
    }

    override fun getIsNightModeOn(): Boolean {
        return pref?.getBoolean(Constants.IS_NIGHT_MODE, false)!!
    }
}