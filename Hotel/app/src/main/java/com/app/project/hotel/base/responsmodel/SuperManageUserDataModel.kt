package com.app.project.hotel.base.responsmodel
import com.squareup.moshi.JsonClass

import com.squareup.moshi.Json

@JsonClass(generateAdapter = true)
data class SuperManageUserDataModel(
    @Json(name = "data")
    val `data`: List<Data?>?,
    @Json(name = "msg")
    val msg: String?
) {
    @JsonClass(generateAdapter = true)
    data class Data(
        @Json(name = "userDate")
        val userDate: String?,
        @Json(name = "userIcon")
        val userIcon: String?,
        @Json(name = "userId")
        val userId: String?,
        @Json(name = "userLocation")
        val userLocation: String?,
        @Json(name = "userName")
        val userName: String?,
        @Json(name = "userPass")
        val userPass: String?,
        @Json(name = "userPhone")
        val userPhone: String?,
        @Json(name = "userStatus")
        val userStatus: Int?,
        @Json(name = "userBadCnt")
        val userBadCnt: Int?
    )
}