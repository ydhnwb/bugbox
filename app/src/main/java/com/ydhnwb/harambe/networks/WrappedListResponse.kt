package com.ydhnwb.harambe.networks

import com.google.gson.annotations.SerializedName

data class WrappedListResponse <T>(
    @SerializedName("status")
    val status : Boolean,
    @SerializedName("message")
    val message : String,
    @SerializedName("data")
    val data : List<T>
)