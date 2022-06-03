package com.app.project.hotel.base.responsmodel

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HotelListDataModel(
    @Json(name = "data")
    val `data`: List<Data?>?,
    @Json(name = "msg")
    val msg: String?
) {
    @JsonClass(generateAdapter = true)
    data class Data(
        @Json(name = "hotelDesc")
        val hotelDesc: String?,
        @Json(name = "hotelIcon")
        val hotelIcon: String?,
        @Json(name = "hotelId")
        val hotelId: String?,
        @Json(name = "hotelLocation")
        val hotelLocation: String?,
        @Json(name = "hotelMinPrice")
        val hotelMinPrice: String?,
        @Json(name = "hotelName")
        val hotelName: String?,
        @Json(name = "hotelPass")
        val hotelPass: String?,
        @Json(name = "hotelPhone")
        val hotelPhone: String?,
        @Json(name = "hotelAvgScore")
        val hotelAvgScore: Int?,
        @Json(name = "newComment")
        val newComment: String?,
        @Json(name = "salesCnt")
        val salesCnt: String?
    )
}