package com.app.project.hotel.base.responsmodel
import com.squareup.moshi.JsonClass

import com.squareup.moshi.Json


@JsonClass(generateAdapter = true)
data class HotelManageOverViewDataModel(
    @Json(name = "data")
    var `data`: List<Data?>? = null,
    @Json(name = "msg")
    var msg: String? = null
) {
    @JsonClass(generateAdapter = true)
    data class Data(
        @Json(name = "hotelId")
        var hotelId: Any? = null,
        @Json(name = "roomDescription")
        var roomDescription: String? = null,
        @Json(name = "roomFeature")
        var roomFeature: String? = null,
        @Json(name = "roomIcon")
        var roomIcon: String? = null,
        @Json(name = "room_id")
        var roomId: String? = null,
        @Json(name = "roomName")
        var roomName: String? = null,
        @Json(name = "roomPrice")
        var roomPrice: String? = null,
        @Json(name = "room_published")
        var roomPublished: Int? = null,
        @Json(name = "room_upLoad")
        var roomUpLoad: Boolean? = null,
        @Json(name = "reason")
        var roomReason: String? = null,
        @Json(name="hotelConfirm")
        var hotelConfirm: Int? = null
    )
}