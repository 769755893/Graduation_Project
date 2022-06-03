package com.app.project.hotel.base.responsmodel
import com.squareup.moshi.JsonClass

import com.squareup.moshi.Json


@JsonClass(generateAdapter = true)
data class HotelCommentDataModel(
    @Json(name = "data")
    val `data`: List<Data?>?,
    @Json(name = "msg")
    val msg: String?
) {
    @JsonClass(generateAdapter = true)
    data class Data(
        @Json(name = "commentId")
        val commentId: String?,
        @Json(name = "goodCnt")
        var goodCnt: String?,
        @Json(name = "hotelId")
        val hotelId: String?,
        @Json(name = "roomId")
        val roomId: String?,
        @Json(name = "roomName")
        val roomName: String?,
        @Json(name = "startTime")
        val startTime: String?,
        @Json(name = "userComment")
        val userComment: String?,
        @Json(name = "userCommentScore")
        val userCommentScore: Int?,
        @Json(name = "userIcon")
        val userIcon: String?,
        @Json(name = "userId")
        val userId: String?,
        @Json(name = "userName")
        val userName: String?
    )
}