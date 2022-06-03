package com.app.project.hotel.base.responsmodel
import com.squareup.moshi.JsonClass

import com.squareup.moshi.Json


@JsonClass(generateAdapter = true)
data class UserOrderListDataModel(
    @Json(name = "data")
    val `data`: List<Data?>?,
    @Json(name = "msg")
    val msg: String?
) {
    @JsonClass(generateAdapter = true)
    data class Data(
        @Json(name = "endTime")
        val endTime: String?,
        @Json(name = "hotelId")
        val hotelId: String?,
        @Json(name = "hotelLocation")
        val hotelLocation: String?,
        @Json(name = "hotelName")
        val hotelName: String?,
        @Json(name = "hotelPhone")
        val hotelPhone: String?,
        @Json(name = "orderId")
        val orderId: String?,
        @Json(name = "orderTime")
        val orderTime: String?,
        @Json(name = "roomDesc")
        val roomDesc: String?,
        @Json(name = "roomFeature")
        val roomFeature: String?,
        @Json(name = "roomIcon")
        val roomIcon: String?,
        @Json(name = "roomId")
        val roomId: String?,
        @Json(name = "roomName")
        val roomName: String?,
        @Json(name = "roomPrice")
        val roomPrice: String?,
        @Json(name = "startTime")
        val startTime: String?,
        @Json(name = "status")
        val status: Int?,
        @Json(name = "userComment")
        var userComment: String?,
        @Json(name = "userCommentScore")
        var userCommentScore: Int?,
        @Json(name = "userIcon")
        val userIcon: String?,
        @Json(name = "userId")
        val userId: String?,
        @Json(name = "userLocation")
        val userLocation: String?,
        @Json(name = "userName")
        val userName: String?,
        @Json(name = "userPhone")
        val userPhone: String?,
        @Json(name = "userOrderConfirm")
        val userOrderConfirm: String?,
        @Json(name = "reason")
        val reason: String?
    )
}