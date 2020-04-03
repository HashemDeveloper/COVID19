package com.project.covid19.model.hopkinsdata

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Stats(
    @SerializedName("confirmed")
    @Expose
    private var confirmed: String,
    @SerializedName("deaths")
    @Expose
    private var deaths: String,
    @SerializedName("recovered")
    @Expose
    private var recovered: String
): Parcelable