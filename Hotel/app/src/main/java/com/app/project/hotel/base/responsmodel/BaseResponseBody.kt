package com.app.project.hotel.base.responsmodel

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Json


@JsonClass(generateAdapter = true)
data class BaseResponseBody(
    @Json(name = "data")
    val `data`: Any?,
    @Json(name = "msg")
    val msg: String?
)