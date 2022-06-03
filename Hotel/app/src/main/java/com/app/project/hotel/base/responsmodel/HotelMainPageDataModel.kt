package com.app.project.hotel.base.responsmodel
import com.squareup.moshi.JsonClass

import com.squareup.moshi.Json


@JsonClass(generateAdapter = true)
data class HotelMainPageDataModel(
    @Json(name = "data")
    val `data`: Data?,
    @Json(name = "msg")
    val msg: String?
) {
    @JsonClass(generateAdapter = true)
    data class Data(
        @Json(name = "hotelContactPhone")
        var hotelContactPhone: String?,
        @Json(name = "hotelDesc")
        var hotelDesc: String?,
        @Json(name = "hotelIcon")
        var hotelIcon: String?,
        @Json(name = "hotelId")
        var hotelId: String?,
        @Json(name = "hotelLocation")
        var hotelLocation: String?,
        @Json(name = "hotelMinPrice")
        var hotelMinPrice: String?,
        @Json(name = "hotelName")
        var hotelName: String?,
        @Json(name = "hotelPass")
        var hotelPass: String?
    )
}