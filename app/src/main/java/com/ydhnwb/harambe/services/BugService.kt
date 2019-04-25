package com.ydhnwb.harambe.services

import com.ydhnwb.harambe.models.BugModel
import com.ydhnwb.harambe.networks.WrappedListResponse
import com.ydhnwb.harambe.networks.WrappedResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface BugService {
    @GET("api/ydh")
    fun all() : Call<WrappedListResponse<BugModel>>

    @GET("api/ydh/{id}")
    fun find(@Path("id") id : String) : Call<WrappedResponse<BugModel>>

    @Multipart
    @POST("api/ydh")
    fun new(@Part photo : MultipartBody.Part, @Part("name") name : RequestBody, @Part("description") description : RequestBody) : Call<WrappedResponse<BugModel>>

    @Multipart
    @POST("api/ydh/{id}")
    fun update(@Part photo : MultipartBody.Part, @Part("name") name : RequestBody, @Part("description") description : RequestBody, @Path("id") id : String) : Call<WrappedResponse<BugModel>>

    @DELETE("api/ydh/{id}")
    fun delete(@Path("id") id : String) : Call<WrappedResponse<BugModel>>

    @FormUrlEncoded
    @POST("api/ydh/search/result")
    fun search(@Field("search") search : String) : Call<WrappedListResponse<BugModel>>
}