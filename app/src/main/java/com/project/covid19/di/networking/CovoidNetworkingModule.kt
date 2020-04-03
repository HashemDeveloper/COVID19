package com.project.covid19.di.networking

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.project.covid19.BuildConfig
import dagger.Module
import dagger.Provides
import okhttp3.*
import timber.log.Timber
import java.net.CookieManager
import java.net.CookiePolicy
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
object CovoidNetworkingModule {
    private const val READ_TIME_OUT: Long = 10000L
    private const val CONNECTION_TIME_OUT: Long = 10000L

    @Singleton
    @Provides
    @JvmStatic
    internal fun provideOkHttpClient(): Call.Factory {
        val cookieManager = CookieManager()
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
        return OkHttpClient.Builder()
            .readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS)
            .connectTimeout(CONNECTION_TIME_OUT, TimeUnit.MILLISECONDS)
            .addNetworkInterceptor(StethoInterceptor())
            .addInterceptor { chain: Interceptor.Chain ->
                val originalRequest: Request = chain.request()
                val request: Request = originalRequest.newBuilder()
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "application/json;charset=UTF-8")
                    .method(originalRequest.method(), originalRequest.body())
                    .build()
                val response: Response = chain.proceed(request)
                debug(response.code().toString())
                try {
                    if (response.code() == 401) {
                        return@addInterceptor response
                    }
                } catch (ex: Exception) {
                    ex.localizedMessage?.let {
                        debug(it)
                    }
                } finally {
                    if (response.body() != null) {
                        response.body()!!.close()
                    }
                }
                response
            }
            .build()
    }
    private fun debug(message: String) {
        if (BuildConfig.DEBUG) {
            Timber.tag("Covid19 --->").d("Code: %s", message)
        }
    }

    @Singleton
    @Named("base_url")
    @JvmStatic
    internal fun provideBaseUrl(baseUrl: String): String {
        return "" //TODO: Base Url
    }
}