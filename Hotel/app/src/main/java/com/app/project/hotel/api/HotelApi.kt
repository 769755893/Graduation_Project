package com.app.project.hotel.api

import com.app.project.hotel.base.responsmodel.BaseResponseBody
import com.app.project.hotel.base.responsmodel.HotelMainPageDataModel
import com.app.project.hotel.base.responsmodel.HotelManageOverViewDataModel
import com.app.project.hotel.base.responsmodel.HotelOrderListDataModel
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface HotelApi {
    @FormUrlEncoded
    @POST("getHotelBaseMsg")
    suspend fun getHotelBaseMsg(@Field("hotelId") hotelId: Int? = null): HotelMainPageDataModel

    @FormUrlEncoded
    @POST("getOrderLength")
    suspend fun getOrderLength(@Field("hotelId") hotelId: Int): BaseResponseBody


    @FormUrlEncoded
    @POST("upLoadRoomMsg")
    suspend fun upLoadRoomMsg(
        @Field("hotelId") hotelId: Int? = null,
        @Field("bitmapStr") bitmapStr: String? = null,
        @Field("roomName") roomName: String? = null,
        @Field("roomDescription") roomDescription: String? = null,
        @Field("roomFeature") roomFeature: String? = null,
        @Field("roomPrice") roomPrice: Int? = null,
        @Field("roomId") roomId: String? = null
    ): BaseResponseBody

    @FormUrlEncoded
    @POST("hotelRoomConfirm")
    suspend fun hotelRoomConfirm(
        @Field("roomId") roomId: String? = null
    ): BaseResponseBody

    @FormUrlEncoded
    @POST("getHotelOverRoomData")
    suspend fun getHotelRoomListData(
        @Field("hotelId") hotelId: Int? = null
    ): HotelManageOverViewDataModel

    @FormUrlEncoded
    @POST("getHotelOrderList")
    suspend fun getHotelOrderList(
        @Field("hotelId") hotelId: Int,
        @Field("offset") offset: Int? = 0
    ): HotelOrderListDataModel

    @FormUrlEncoded
    @POST("upLoadHotelIconMsg")
    suspend fun upLoadHotelIconMsg(
        @Field("iconString") iconString: String? = null,
        @Field("hotelId") hotelId: Int? = null,
        @Field("hotelDesc") hotelDesc: String? = null,
        @Field("hotelMinPrice") hotelMinPrice: String? = null
    ): BaseResponseBody

    @FormUrlEncoded
    @POST("orderDone")
    suspend fun hotelOrderDone(
        @Field("orderId") orderId: String? = null,
        @Field("hotelId") hotelId: Int? = null
    ): BaseResponseBody

    @FormUrlEncoded
    @POST("orderConfirm")
    suspend fun hotelConfirm(
        @Field("orderId") orderId: String? = null,
        @Field("hotelId") hotelId: Int? = null
    ): BaseResponseBody

    @FormUrlEncoded
    @POST("orderReject")
    suspend fun hotelReject(
        @Field("orderId") orderId: String? = null,
        @Field("hotelId") hotelId: Int? = null,
        @Field("reason") reason: String? = null
    ): BaseResponseBody
}