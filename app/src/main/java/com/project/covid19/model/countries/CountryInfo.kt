package com.project.covid19.model.countries

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CountryInfo(
    @SerializedName("_id")
    @Expose
    private var id: Int,
    @SerializedName("iso2")
    @Expose
    private var iso2: String?,
    @SerializedName("iso3")
    @Expose
    private var iso3: String?,
    @SerializedName("lat")
    @Expose
    private var lat: Long?,
    @SerializedName("long")
    @Expose
    private var lon: Long?,
    @SerializedName("flag")
    @Expose
    private var flag: String?
): Parcelable