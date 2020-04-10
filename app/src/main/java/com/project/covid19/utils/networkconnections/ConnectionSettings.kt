package com.project.covid19.utils.networkconnections

import android.content.Intent
import android.provider.Settings
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.Snackbar
import com.project.covid19.R

class ConnectionSettings(private var fragmentActivity: FragmentActivity, private var view: View) {
    private var snackbar: Snackbar?= null
    fun build() {
        this.snackbar = Snackbar.make(this.view, this.fragmentActivity.resources.getText(R.string.not_connected_internet), Snackbar.LENGTH_INDEFINITE)
    }
    fun initWifiSetting(isConnected: Boolean, nightMode: Boolean) {
        if (nightMode) {
            snackbar?.view!!.setBackgroundColor(ContextCompat.getColor(this.fragmentActivity, R.color.black))
            snackbar?.setActionTextColor(ContextCompat.getColor(this.fragmentActivity, R.color.red_900))
            snackbar?.setTextColor(ContextCompat.getColor(this.fragmentActivity, R.color.white))
        } else {
            snackbar?.view!!.setBackgroundColor(ContextCompat.getColor(this.fragmentActivity, R.color.white))
            snackbar?.setActionTextColor(ContextCompat.getColor(this.fragmentActivity, R.color.red_900))
            snackbar?.setTextColor(ContextCompat.getColor(this.fragmentActivity, R.color.black))
        }

        snackbar?.setAction(this.fragmentActivity.resources.getText(R.string.settings)) { v ->
            val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            this.fragmentActivity.startActivity(intent)
        }
        if (isConnected) {
            snackbar?.dismiss()
        } else {
            snackbar?.show()
        }
    }

    fun getSnackBar(): Snackbar {
        return this.snackbar!!
    }
}