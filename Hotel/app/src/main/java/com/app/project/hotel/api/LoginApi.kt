package com.app.project.hotel.api

import com.app.project.hotel.base.responsmodel.BaseResponseBody
import retrofit2.http.*

interface LoginApi {
    @FormUrlEncoded
    @POST("userLogin")
    suspend fun userLogin(
        @Field("userId") userName: String? = null,
        @Field("userPass") userPass: String? = null
    ): BaseResponseBody

    @FormUrlEncoded
    @POST("getUserIcon")
    suspend fun getUserIcon(
        @Field("userId") userId: String? = null
    ): BaseResponseBody

    @FormUrlEncoded
    @POST("loginHotel")
    suspend fun loginHotel(
        @Field("hotelId") hotelId: Int? = null,
        @Field("hotelPass") hotelPass: String? = null
    ): BaseResponseBody

    @FormUrlEncoded
    @POST("userSign")
    suspend fun userSign(
        @Field("userId") userName: String? = null,
        @Field("userPass") userPass: String? = null,
        @Field("userDate") userDate: String? = null
    ): BaseResponseBody

    @FormUrlEncoded
    @POST("forgetQuery")
    suspend fun forgetQuery(
        @Field("userId") userName: String? = null,
        @Field("userDate") userDate: String? = null
    ): BaseResponseBody

    @FormUrlEncoded
    @POST("signHotel")
    suspend fun signHotel(
        @Field("hotelName") hotelName: String? = null,
        @Field("hotelCode") hotelCode: String? = null,
        @Field("hotelPhone") hotelPhone: String? = null,
        @Field("hotelLocation") hotelLocation: String? = null
    ): BaseResponseBody
}