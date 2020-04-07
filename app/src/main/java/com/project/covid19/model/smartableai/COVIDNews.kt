package com.project.covid19.model.smartableai

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class COVIDNews(
    @SerializedName("path")
    @Expose
    var path: String,
    @SerializedName("title")
    @Expose
    var title: String,
    @SerializedName("excerpt")
    @Expose
    var excerpt: String,
    @SerializedName("heat")
    @Expose
    var heat: Int,
    @SerializedName("tags")
    @Expose
    var tags: List<String>?= arrayListOf(),
    @SerializedName("type")
    @Expose
    var type: String,
    @SerializedName("webUrl")
    @Expose
    var webUrl: String,
    @SerializedName("ampWebUrl")
    @Expose
    var ampWebUrl: String,
    @SerializedName("cdnAmpWebUrl")
    @Expose
    var cdnAmpWebUrl: String,
    @SerializedName("publishedDateTime")
    @Expose
    var publishedDateTime: String,
    @SerializedName("updatedDateTime")
    @Expose
    var updatedDateTime: String?= "",
    @SerializedName("provider")
    @Expose
    var provider: @RawValue NewsProvider?,
    @SerializedName("images")
    @Expose
    var images: List<Images>?= arrayListOf(),
    @SerializedName("locale")
    @Expose
    var locale: String,
    @SerializedName("categories")
    @Expose
    var categories: List<String>?= arrayListOf(),
    @SerializedName("topics")
    @Expose
    var topics: List<String>?= arrayListOf()
): Parcelable