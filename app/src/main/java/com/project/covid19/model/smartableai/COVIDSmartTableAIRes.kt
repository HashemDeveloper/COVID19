package com.project.covid19.model.smartableai

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class COVIDSmartTableAIRes(
    @SerializedName("location")
    @Expose
    var location: Location?,
    @SerializedName("updatedDateTime")
    @Expose
    var updatedDateTime: String,
    @SerializedName("news")
    @Expose
    var news: List<COVIDNews>?= arrayListOf()
):Parcelable