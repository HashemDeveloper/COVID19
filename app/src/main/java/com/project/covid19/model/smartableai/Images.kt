package com.project.covid19.model.smartableai

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Images(
    @SerializedName("url")
    @Expose
    var url: String?,
    @SerializedName("width")
    @Expose
    var width: Int?,
    @SerializedName("height")
    @Expose
    var height: Int?,
    @SerializedName("title")
    @Expose
    var title: String?,
    @SerializedName("attribution")
    @Expose
    var attribution: String?
): Parcelable