package com.ydhnwb.harambe.models

import com.google.gson.annotations.SerializedName

data class BugModel(@SerializedName("id") var id : Int?,
    @SerializedName("name") var name : String?,
    @SerializedName("description") var description : String?,
    @SerializedName("photo") var photo : String?
){
    constructor() : this(null, null, null, null)
}