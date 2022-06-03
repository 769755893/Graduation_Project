package com.app.project.hotel.ui.fragments.home.user.userhotelroomlist.roomlist.orderdialog

data class UserOrderData(
    var starTime: String? = null,
    var endTime: String? = null,
    var roomId: String? = null,
    var roomName: String? = null,
    var roomDesc: String? = null,
    var roomFeature: String? = null,
    var dayCount: Int? = null,

    var userId: String? = null,
    var userName: String ?= null,
    var userPhone: String ? = null,

    var roomPrice: String? = null,

    var hotelId : String ? = null,
    var hotelName: String ? = null,
    var hotelLocation: String? = null
)