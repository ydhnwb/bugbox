package com.ydhnwb.harambe.networks

import com.google.gson.annotations.SerializedName

data class WrappedResponse <T>(
    @SerializedName("status")
    val status : Boolean,
    @SerializedName("message")
    val message : String,
    @SerializedName("data")
    val data : T
)