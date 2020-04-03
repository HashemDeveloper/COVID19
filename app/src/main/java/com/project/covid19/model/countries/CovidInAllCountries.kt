package com.project.covid19.model.countries

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CovidInAllCountries(
    @SerializedName("country")
    @Expose
    private var country: String,
    @SerializedName("countryInfo")
    @Expose
    private var countryInfo: CountryInfo,
    @SerializedName("cases")
    @Expose
    private var cases: Int,
    @SerializedName("todayCases")
    @Expose
    private var todayCases: Int,
    @SerializedName("deaths")
    @Expose
    private var deaths: Int,
    @SerializedName("todayDeaths")
    @Expose
    private var todayDeaths: Int,
    @Expose
    private var recovered: Int,
    @SerializedName("active")
    @Expose
    private var active: Int,
    @SerializedName("critical")
    @Expose
    private var critical: Int,
    @SerializedName("casesPerOneMillion")
    @Expose
    private var casesPerOneMillion: Double,
    @SerializedName("deathsPerOneMillion")
    @Expose
    private var deathsPerOneMillion: Double,
    @SerializedName("updated")
    @Expose
    private var updated: Int
): Parcelable