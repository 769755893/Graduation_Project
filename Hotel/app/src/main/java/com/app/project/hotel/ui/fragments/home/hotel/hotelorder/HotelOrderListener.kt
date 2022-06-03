package com.app.project.hotel.ui.fragments.home.hotel.hotelorder

interface HotelOrderListener {
    fun hotelConfirm(orderId: String, position: Int)
    fun hotelCancel(orderId: String, hotelId: Int, position: Int)
    fun orderDone(orderId: String, hotelId: Int, position: Int)
}