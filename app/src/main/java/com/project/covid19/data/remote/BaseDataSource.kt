package com.project.covid19.data.remote

import com.project.covid19.BuildConfig
import retrofit2.Response
import timber.log.Timber

abstract class BaseDataSource {
    protected suspend fun <T> getResult(call: suspend() -> Response<T>): DataHandler<T> {
        return try {
            val response: Response<T> = call()
            if (response.isSuccessful) {
                val body: T? = response.body()
                if (body != null) {
                    DataHandler.onSuccess(
                        body
                    )
                } else {
                    DataHandler.onError(
                        "Response body was empty"
                    )
                }
            } else {
                error("${response.code()}: ${response.message()}")
            }
        } catch (ex: Exception) {
            error(ex.message ?: ex.toString())
        }
    }

    private fun <T> error(message: String?): DataHandler<T> {
        if (BuildConfig.DEBUG) {
            Timber.e(message)
        }
        return DataHandler.onError("Call failed for following reason: $message")
    }
}