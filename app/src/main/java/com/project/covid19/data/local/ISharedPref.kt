package com.project.covid19.data.local

interface ISharedPref {
    fun setIsNightModeOn(isNightModeOn: Boolean)
    fun getIsNightModeOn(): Boolean
}