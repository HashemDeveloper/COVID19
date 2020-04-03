package com.project.covid19.model.hopkinsdata

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Coordinates(
    @SerializedName("latitude")
    @Expose
    private var lattitude: String,
    @SerializedName("longitude")
    @Expose
    private var longitude: String
): Parcelable