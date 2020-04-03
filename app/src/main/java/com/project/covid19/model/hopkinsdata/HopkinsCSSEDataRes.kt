package com.project.covid19.model.hopkinsdata

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HopkinsCSSEDataRes(
    @SerializedName("country")
    @Expose
    private var country: String,
    @SerializedName("province")
    @Expose
    private var province: String,
    @SerializedName("city")
    @Expose
    private var city: String,
    @SerializedName("updatedAt")
    @Expose
    private var updatedAt: String,
    @SerializedName("stats")
    @Expose
    private var stats: Stats?,
    @SerializedName("coordinates")
    @Expose
    private var coordinates: Coordinates?
): Parcelable