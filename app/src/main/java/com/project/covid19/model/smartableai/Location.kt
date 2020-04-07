package com.project.covid19.model.smartableai

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Location(
    @SerializedName("long")
    @Expose
    var longitude: Double,
    @SerializedName("countryOrRegion")
    @Expose
    var countryOrRegion: String?,
    @SerializedName("provinceOrState")
    @Expose
    var provinceOrState: String?,
    @SerializedName("county")
    @Expose
    var county: String?,
    @SerializedName("isoCode")
    @Expose
    var isoCode: String,
    @SerializedName("lat")
    @Expose
    var latitude: Double
): Parcelable