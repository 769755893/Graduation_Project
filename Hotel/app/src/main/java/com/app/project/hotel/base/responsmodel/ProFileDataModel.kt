package com.app.project.hotel.base.responsmodel
import com.squareup.moshi.JsonClass

import com.squareup.moshi.Json


@JsonClass(generateAdapter = true)
data class ProFileDataModel(
    @Json(name = "data")
    var `data`: Data?,
    @Json(name = "msg")
    var msg: String?
) {
    @JsonClass(generateAdapter = true)
    data class Data(
        @Json(name = "userBz")
        var userBz: String?,
        @Json(name = "userDate")
        var userDate: String?,
        @Json(name = "userIcon")
        var userIcon: String?,
        @Json(name = "userId")
        var userId: String?,
        @Json(name = "userLocation")
        var userLocation: String?,
        @Json(name = "userName")
        var userName: String?,
        @Json(name = "userPass")
        var userPass: String?,
        @Json(name = "userPhone")
        var userPhone: String?,
        @Json(name = "userState")
        var userState: Int?
    )
}