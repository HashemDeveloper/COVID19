package com.project.covid19.model.hopkinsdata

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Stats(
    @SerializedName("confirmed")
    @Expose
    var confirmed: String,
    @SerializedName("deaths")
    @Expose
    var deaths: String,
    @SerializedName("recovered")
    @Expose
    var recovered: String
): Parcelable