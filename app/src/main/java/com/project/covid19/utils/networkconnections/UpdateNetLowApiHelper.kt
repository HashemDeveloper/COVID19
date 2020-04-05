package com.project.covid19.utils.networkconnections

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
import com.project.covid19.R
import javax.inject.Inject

class UpdateNetLowApiHelper @Inject constructor(private val context: Context): IUpdateNetLowApiHelper {
    private var connectivityManager: ConnectivityManager?= null

    init {
        this.connectivityManager = this.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
    override fun updateNetwork() {
        updateNetworkChange()
    }

    private fun updateNetworkChange() {
        when (isNetworkConnected()) {
            COVIDNetType.WIFI_DATA -> {
                Toast.makeText(this.context, R.string.using_wifi, Toast.LENGTH_SHORT).show()
            }
            COVIDNetType.MOBILE_DATA -> {
                Toast.makeText(this.context, R.string.using_mobile_data, Toast.LENGTH_SHORT).show()
            }
            COVIDNetType.UNAUTHORIZED_INTERNET -> {
                Toast.makeText(this.context, R.string.unauthorized_internet_connection, Toast.LENGTH_SHORT).show()
            }
            COVIDNetType.NO_NETWORK -> {
                Toast.makeText(this.context, R.string.not_connected_internet, Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun isNetworkConnected(): COVIDNetType? {
        var covidNetType: COVIDNetType?= null
        this.connectivityManager?.let {connectionManager ->
            val isWifiConnected: Boolean = connectionManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!!.isConnected
            val isMobileNetConnected: Boolean = connectionManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!!.isConnected
            if (isWifiConnected || isMobileNetConnected) {
                if (PingNetwork.isOnline) {
                    covidNetType = when {
                        isWifiConnected -> {
                            COVIDNetType.WIFI_DATA
                        }
                        isMobileNetConnected -> {
                            COVIDNetType.MOBILE_DATA
                        }
                        else -> {
                            COVIDNetType.UNAUTHORIZED_INTERNET
                        }
                    }
                }
            } else {
                covidNetType = COVIDNetType.NO_NETWORK
            }
        }
        return if (covidNetType != null) {
            covidNetType
        } else {
            null
        }
    }
}