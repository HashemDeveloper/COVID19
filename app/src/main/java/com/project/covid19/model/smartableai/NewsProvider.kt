package com.project.covid19.model.smartableai

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class NewsProvider(
    @SerializedName("name")
    @Expose
    var name: String?,
    @SerializedName("domain")
    @Expose
    var domain: String?,
    @SerializedName("images")
    @Expose
    var images: List<Images>?= arrayListOf(),
    @SerializedName("publishers")
    @Expose
    var publishers: List<String>?= arrayListOf()
): Parcelable