package com.app.project.hotel.base.responsmodel
import com.squareup.moshi.JsonClass

import com.squareup.moshi.Json

@JsonClass(generateAdapter = true)
data class UserHotelRoomDataModel(
    @Json(name = "data")
    var `data`: List<Data?>?,
    @Json(name = "msg")
    var msg: String?
) {
    @JsonClass(generateAdapter = true)
    data class Data(
        @Json(name = "roomDesc")
        var roomDesc: String?,
        @Json(name = "roomFeature")
        var roomFeature: String?,
        @Json(name = "roomIcon")
        var roomIcon: String?,
        @Json(name = "roomId")
        var roomId: String?,
        @Json(name = "roomName")
        var roomName: String?,
        @Json(name = "roomPrice")
        var roomPrice: String?
    )
}
