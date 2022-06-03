package com.app.project.hotel.api

import com.app.project.hotel.base.responsmodel.*
import io.reactivex.Single
import retrofit2.http.*

interface UserApi {

    @FormUrlEncoded
    @POST("userPay")
    fun userPay(
        @Field("userId") userId: String?,
        @Field("money") money: Int
    ): Single<BaseResponseBody>

    @FormUrlEncoded
    @POST("getUserMoney")
    fun getUserMoney(@Field("userId") userId: String?): Single<BaseResponseBody>


    @FormUrlEncoded
    @POST("userRecharge")
    fun userRecharge(
        @Field("userId") userId: String?,
        @Field("rechargeKey") rechargeKey: String?
    ): Single<BaseResponseBody>

    @GET("getHotelCount")
    fun getHotelCount(): Single<BaseResponseBody>

    @FormUrlEncoded
    @POST("getHotelList")
    fun getHotelList(
        @Field("sortType") sortType: Int,
        @Field("offset") offset: Int,
        @Field("keyWord") keyWord: String? = ""
    ): Single<HotelListDataModel>

    @FormUrlEncoded
    @POST("getHotelRoomList")
    fun getHotelRoomList(
        @Field("hotelId") hotelId: Int? = null
    ): Single<UserHotelRoomDataModel>

    @FormUrlEncoded
    @POST("getUserMsg")
    fun getUserMsg(@Field("userId") userId: String): Single<ProFileDataModel>

    @FormUrlEncoded
    @POST("upLoadUserMsg")
    fun upLoadUserMsg(
        @Field("userIcon") userIcon: String? = null,
        @Field("userName") userName: String? = null,
        @Field("userPass") userPass: String? = null,
        @Field("userLocation") userLocation: String? = null,
        @Field("userPhone") userPhone: String? = null,
        @Field("userId") userId: String? = null,
        @Field("userBz") userBz: String? = null
    ): Single<BaseResponseBody>

    @FormUrlEncoded
    @POST("updateOrderState")
    fun updateOrderState(
        @Field("userId") orderId: String? = null,
        @Field("state") state: Int? = null
    ): Single<BaseResponseBody>

    @FormUrlEncoded
    @POST("orderPay")
    fun orderPay(
        @Field("hotelId") hotelId: String? = null,
        @Field("userId") userId: String? = null,
        @Field("roomId") roomId: String? = null,
        @Field("startTime") starTime: String? = null,
        @Field("endTime") endTime: String? = null,
        @Field("roomPrice") roomPrice: String? = null
    ): Single<BaseResponseBody>

    @FormUrlEncoded
    @POST("getOrderList")
    fun getOrderList(
        @Field("userId") userId: String? = null,
        @Field("hotelId") hotelId: String? = null,
        @Field("offset") offset: Int? = null,
        @Field("orderTimeType") orderTimeType: Int
    ): Single<UserOrderListDataModel>

    @FormUrlEncoded
    @POST("getOrderLength")
    fun getOrderLength(
        @Field("userId") userId: String
    ): Single<BaseResponseBody>

    @FormUrlEncoded
    @POST("cancelOrder")
    fun cancelOrder(
        @Field("orderId") orderId: String? = null,
        @Field("userId") userId: String? = null
    ): Single<BaseResponseBody>

    @FormUrlEncoded
    @POST("hotelOrderStatusChange")
    fun hotelOrderStatusChange(
        @Field("orderId") orderId: String? = null,
        @Field("status") status: Int? = null
    ): Single<BaseResponseBody>

    @FormUrlEncoded
    @POST("getHotelCommentList")
    fun getHotelCommentList(@Field("hotelId") hotelId: Int? = null): Single<HotelCommentDataModel>

    @FormUrlEncoded
    @POST("goodClick")
    fun goodClick(@Field("commentId") commentId: String? = null): Single<BaseResponseBody>

    @FormUrlEncoded
    @POST("upLoadUserComment")
    fun upLoadUserComment(
        @Field("orderId") orderId: String? = null,
        @Field("commentId") commentId: String? = null,
        @Field("hotelId") hotelId: Int? = null,
        @Field("userId") userId: String? = null,
        @Field("userComment") userComment: String? = null,
        @Field("userCommentScore") userCommentScore: Int? = null,
        @Field("startTime") startTime: String? = null,
        @Field("roomId") roomId: String? = null
    ): Single<BaseResponseBody>

    @FormUrlEncoded
    @POST("userOrderConfirm")
    fun userOrderConfirm(@Field("orderId") orderId: String? = null): Single<BaseResponseBody>
}