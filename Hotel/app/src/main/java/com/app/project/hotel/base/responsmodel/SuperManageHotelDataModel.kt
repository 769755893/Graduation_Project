package com.app.project.hotel.base.responsmodel
import com.squareup.moshi.JsonClass

import com.squareup.moshi.Json


@JsonClass(generateAdapter = true)
data class SuperManageHotelDataModel(
    @Json(name = "data")
    var `data`: List<Data?>?,
    @Json(name = "msg")
    var msg: String?
) {
    @JsonClass(generateAdapter = true)
    data class Data(
        @Json(name = "childSize")
        var childSize: Int?,
        @Json(name = "foldState")
        var foldState: Boolean?,
        @Json(name = "hotelBadCnt")
        var hotelBadCnt: Int?,
        @Json(name = "hotelIcon")
        var hotelIcon: String?,
        @Json(name = "hotelId")
        var hotelId: String?,
        @Json(name = "hotelLocation")
        var hotelLocation: String?,
        @Json(name = "hotelName")
        var hotelName: String?,
        @Json(name = "hotelPass")
        var hotelPass: String?,
        @Json(name = "hotelPhone")
        var hotelPhone: String?,
        @Json(name = "hotelState")
        var hotelState: Int?,
        @Json(name = "parent")
        var parent: Boolean?,
        @Json(name = "roomDescription")
        var roomDescription: String?,
        @Json(name = "roomFeature")
        var roomFeature: String?,
        @Json(name = "roomIcon")
        var roomIcon: String?,
        @Json(name = "room_id")
        var roomId: String?,
        @Json(name = "roomName")
        var roomName: String?,
        @Json(name = "roomPrice")
        var roomPrice: String?,
        @Json(name = "room_published")
        var roomPublished: Int?,
        @Json(name = "room_upLoad")
        var roomUpLoad: Boolean?
    )
}