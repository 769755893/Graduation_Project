package com.app.project.hotel.api

import com.app.project.hotel.base.responsmodel.BaseResponseBody
import com.app.project.hotel.base.responsmodel.SuperManageHotelDataModel
import com.app.project.hotel.base.responsmodel.SuperManageUserDataModel
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface SuperApi {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(@Field("key") key: String? = null): BaseResponseBody

    @FormUrlEncoded
    @POST("queryHotelList")
    suspend fun queryHotelList(
        @Field("hotelId") hotelId: Int? = null,
        @Field("hotelName") hotelName: String? = null,
        @Field("hotelLocation") hotelLocation: String? = null,
        @Field("offset") offset: Int? = null,
        @Field("sortType") sortType: Int
    ): SuperManageHotelDataModel

    @FormUrlEncoded
    @POST("queryHotelRoomList")
    suspend fun queryHotelRoomList(
        @Field("hotelId") hotelId: Int? = null
    ): SuperManageHotelDataModel

    @FormUrlEncoded
    @POST("queryUserList")
    suspend fun queryUserList(
        @Field("userId") userId: String? = null,
        @Field("userName") userName: String? = null,
        @Field("offset") offset: Int? = null,
        @Field("sortType") sortType: Int
        ): SuperManageUserDataModel

    @FormUrlEncoded
    @POST("changedRoomStatus")
    suspend fun changedRoomStatus(
        @Field("roomId") roomId: String? = null,
        @Field("publishStatus") publishStatus: Int? = null,
        @Field("reason") reason: String? = null
    ): BaseResponseBody

    @FormUrlEncoded
    @POST("changeHotelState")
    suspend fun changeHotelState(
        @Field("hotelId") hotelId: String? = null,
        @Field("state") state: Int? = null
    ): BaseResponseBody

    @FormUrlEncoded
    @POST("hotelEnterRequest")
    suspend fun hotelEnterRequest(@Field("key") key: String? = null): BaseResponseBody

    @FormUrlEncoded
    @POST("superEnterRequest")
    suspend fun superEnterRequest(@Field("key") key: String? = null): BaseResponseBody

    @FormUrlEncoded
    @POST("upLoadUserState")
    suspend fun upLoadUserState(
        @Field("userId") userId: String,
        @Field("userState") userState: Int
    ): BaseResponseBody


    @POST("getUserLength")
    suspend fun getUserLength(): BaseResponseBody

    @POST("getHotelLength")
    suspend fun getHotelLength(): BaseResponseBody
}

