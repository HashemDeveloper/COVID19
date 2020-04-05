package com.project.covid19.data.remote

import com.project.covid19.model.countries.CovidInAllCountries
import com.project.covid19.model.hopkinsdata.HopkinsCSSEDataRes
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface CovidAPI {
    @Headers("Content-Type: application/json", "User-Agent: $APP_NAME")
    @GET("v2/jhucsse")
    suspend fun fetchCSSEData(): Response<List<HopkinsCSSEDataRes>>
    @Headers("Content-Type: application/jsoon", "User-Agent: $APP_NAME")
    @GET("countries")
    suspend fun fetchAllCountriesData(): Response<CovidInAllCountries>
    companion object {
        private const val APP_NAME: String = "COVID19"
    }
}