package com.project.covid19.data.remote

data class DataHandler<out T>(val status: Status, val data: T?, val message: String?) {
    enum class Status {
        ERROR,
        SUCCESS,
        LOADING
    }
    companion object {
        fun <T> onLoading(data: T?= null): DataHandler<T> {
            return DataHandler(
                Status.LOADING,
                data,
                null
            )
        }
        fun <T> onSuccess(data: T): DataHandler<T> {
            return DataHandler(
                Status.SUCCESS,
                data,
                null
            )
        }
        fun <T> onError(message: String, data: T?= null): DataHandler<T> {
            return DataHandler(
                Status.ERROR,
                data,
                message
            )
        }
    }
}