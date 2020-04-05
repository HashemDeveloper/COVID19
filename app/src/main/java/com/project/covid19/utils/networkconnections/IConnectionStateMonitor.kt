package com.project.covid19.utils.networkconnections

import androidx.lifecycle.LiveData

interface IConnectionStateMonitor {
    fun isUsingWifiLiveData(): LiveData<Boolean>
    fun isUsingMobileData(): LiveData<Boolean>
    fun isConnectedNoInternetLiveData(): LiveData<Boolean>
    fun updateConnection()
    fun getObserver(): ConnectionStateMonitor
}