package com.project.covid19.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.project.covid19.utils.Constants
import com.project.covid19.utils.networkconnections.IUpdateNetLowApiHelper
import dagger.android.AndroidInjection
import javax.inject.Inject

class Covid19BroadCastReceiver @Inject constructor(): BroadcastReceiver() {
    @Inject
    lateinit var iUpdateNetLowApiHelper: IUpdateNetLowApiHelper

    override fun onReceive(context: Context?, intent: Intent?) {
        AndroidInjection.inject(this, context)
        when (intent!!.action) {
            Constants.CONNECTIVITY_ACTION -> {
                this.iUpdateNetLowApiHelper.updateNetwork()
            }
        }
    }
}