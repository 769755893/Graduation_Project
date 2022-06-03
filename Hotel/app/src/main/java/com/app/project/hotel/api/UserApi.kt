package com.app.project.hotel.api

import com.app.project.hotel.base.responsmodel.*
import retrofit2.http.*

interface UserApi {

    @FormUrlEncoded
    @POST("userPay")
    suspend fun userPay(
        @Field("userId") userId: String?,
        @Field("money") money: Int
    ): BaseResponseBody

    @FormUrlEncoded
    @POST("getUserMoney")
    suspend fun getUserMoney(@Field("userId") userId: String?): BaseResponseBody


    @FormUrlEncoded
    @POST("userRecharge")
    suspend fun userRecharge(
        @Field("userId") userId: String?,
        @Field("rechargeKey") rechargeKey: String?
    ): BaseResponseBody

    @GET("getHotelCount")
    suspend fun getHotelCount(): BaseResponseBody

    @FormUrlEncoded
    @POST("getHotelList")
    suspend fun getHotelList(
        @Field("sortType") sortType: Int,
        @Field("offset") offset: Int,
        @Field("keyWord") keyWord: String? = ""
    ): HotelListDataModel

    @FormUrlEncoded
    @POST("getHotelRoomList")
    suspend fun getHotelRoomList(
        @Field("hotelId") hotelId: Int? = null
    ): UserHotelRoomDataModel

    @FormUrlEncoded
    @POST("getUserMsg")
    suspend fun getUserMsg(@Field("userId") userId: String): ProFileDataModel

    @FormUrlEncoded
    @POST("upLoadUserMsg")
    suspend fun upLoadUserMsg(
        @Field("userIcon") userIcon: String? = null,
        @Field("userName") userName: String? = null,
        @Field("userPass") userPass: String? = null,
        @Field("userLocation") userLocation: String? = null,
        @Field("userPhone") userPhone: String? = null,
        @Field("userId") userId: String? = null,
        @Field("userBz") userBz: String? = null
    ): BaseResponseBody

    @FormUrlEncoded
    @POST("updateOrderState")
    suspend fun updateOrderState(
        @Field("userId") orderId: String? = null,
        @Field("state") state: Int? = null
    ): BaseResponseBody

    @FormUrlEncoded
    @POST("orderPay")
    suspend fun orderPay(
        @Field("hotelId") hotelId: String? = null,
        @Field("userId") userId: String? = null,
        @Field("roomId") roomId: String? = null,
        @Field("startTime") starTime: String? = null,
        @Field("endTime") endTime: String? = null,
        @Field("roomPrice") roomPrice: String? = null
    ): BaseResponseBody

    @FormUrlEncoded
    @POST("getOrderList")
    suspend fun getOrderList(
        @Field("userId") userId: String? = null,
        @Field("hotelId") hotelId: String? = null,
        @Field("offset") offset: Int? = null,
        @Field("orderTimeType") orderTimeType: Int
    ): UserOrderListDataModel

    @FormUrlEncoded
    @POST("getOrderLength")
    suspend fun getOrderLength(
        @Field("userId") userId: String
    ): BaseResponseBody

    @FormUrlEncoded
    @POST("cancelOrder")
    suspend fun cancelOrder(
        @Field("orderId") orderId: String? = null,
        @Field("userId") userId: String? = null
    ): BaseResponseBody

    @FormUrlEncoded
    @POST("hotelOrderStatusChange")
    suspend fun hotelOrderStatusChange(
        @Field("orderId") orderId: String? = null,
        @Field("status") status: Int? = null
    ): BaseResponseBody

    @FormUrlEncoded
    @POST("getHotelCommentList")
    suspend fun getHotelCommentList(@Field("hotelId") hotelId: Int? = null): HotelCommentDataModel

    @FormUrlEncoded
    @POST("goodClick")
    suspend fun goodClick(@Field("commentId") commentId: String? = null): BaseResponseBody

    @FormUrlEncoded
    @POST("upLoadUserComment")
    suspend fun upLoadUserComment(
        @Field("orderId") orderId: String? = null,
        @Field("commentId") commentId: String? = null,
        @Field("hotelId") hotelId: Int? = null,
        @Field("userId") userId: String? = null,
        @Field("userComment") userComment: String? = null,
        @Field("userCommentScore") userCommentScore: Int? = null,
        @Field("startTime") startTime: String? = null,
        @Field("roomId") roomId: String? = null
    ): BaseResponseBody

    @FormUrlEncoded
    @POST("userOrderConfirm")
    suspend fun userOrderConfirm(@Field("orderId") orderId: String? = null): BaseResponseBody
}