package com.project.covid19.data.local

import android.content.SharedPreferences

interface ISharedPref {
    fun setIsNightModeOn(isNightModeOn: Boolean)
    fun getIsNightModeOn(): Boolean
    fun registerOnSharedPrefListener(sharedPrefListener: SharedPreferences.OnSharedPreferenceChangeListener)
    fun unregisterOnSharedPrefListener(sharedPrefListener: SharedPreferences.OnSharedPreferenceChangeListener)
}