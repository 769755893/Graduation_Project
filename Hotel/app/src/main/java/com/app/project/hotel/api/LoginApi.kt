package com.app.project.hotel.api

import com.app.project.hotel.base.responsmodel.BaseResponseBody
import io.reactivex.Single
import retrofit2.http.*

interface LoginApi {
    @FormUrlEncoded
    @POST("userLogin")
    fun userLogin(
        @Field("userId") userName: String? = null,
        @Field("userPass") userPass: String? = null
    ): Single<BaseResponseBody>

    @FormUrlEncoded
    @POST("getUserIcon")
    fun getUserIcon(
        @Field("userId") userId: String? = null
    ): Single<BaseResponseBody>

    @FormUrlEncoded
    @POST("loginHotel")
    fun loginHotel(
        @Field("hotelId") hotelId: Int? = null,
        @Field("hotelPass") hotelPass: String? = null
    ): Single<BaseResponseBody>

    @FormUrlEncoded
    @POST("userSign")
    fun userSign(
        @Field("userId") userName: String? = null,
        @Field("userPass") userPass: String? = null,
        @Field("userDate") userDate: String? = null
    ): Single<BaseResponseBody>

    @FormUrlEncoded
    @POST("forgetQuery")
    fun forgetQuery(
        @Field("userId") userName: String? = null,
        @Field("userDate") userDate: String? = null
    ): Single<BaseResponseBody>

    @FormUrlEncoded
    @POST("signHotel")
    fun signHotel(
        @Field("hotelName") hotelName: String? = null,
        @Field("hotelCode") hotelCode: String? = null,
        @Field("hotelPhone") hotelPhone: String? = null,
        @Field("hotelLocation") hotelLocation: String? = null
    ): Single<BaseResponseBody>
}