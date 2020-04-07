package com.project.covid19.data.remote

import com.project.covid19.model.countries.CovidInAllCountries
import com.project.covid19.model.hopkinsdata.HopkinsCSSEDataRes
import com.project.covid19.model.smartableai.COVIDSmartTableAIRes
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Url

interface CovidAPI {
    @Headers("Content-Type: application/json", "User-Agent: $APP_NAME")
    @GET("v2/jhucsse")
    suspend fun fetchCSSEData(): Response<List<HopkinsCSSEDataRes>>
    @Headers("Content-Type: application/json", "User-Agent: $APP_NAME")
    @GET("countries")
    suspend fun fetchAllCountriesData(): Response<CovidInAllCountries>

    //THIS API IS FROM SMARTABLEAPI News For Corona Virus
    @Headers("Content-Type: application/json", "Cache-Control: no-cache", "Subscription-Key: $SMART_TABLE_SUB_KEY")
    @GET
    suspend fun getNewsOfCOVID19(@Url url: String): Response<COVIDSmartTableAIRes>

    companion object {
        private const val APP_NAME: String = "COVID19"
        private const val SMART_TABLE_SUB_KEY = "fc4594a79fe04110b58f39bd2807d910"
    }
}