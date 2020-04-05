package com.project.covid19.utils.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.project.covid19.data.remote.DataHandler

fun <T> covidLiveDataExt(network: suspend() ->DataHandler<T>): LiveData<DataHandler<T>> =
    liveData {
        emit(DataHandler.onLoading<T>())
        val responseStatus: DataHandler<T> = network.invoke()
        if (responseStatus.status == DataHandler.Status.SUCCESS) {
            emit(DataHandler.onSuccess(responseStatus.data!!))
        } else if (responseStatus.status == DataHandler.Status.LOADING) {
            emit(DataHandler.onError(responseStatus.message!!))
        }
    }