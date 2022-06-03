package com.app.project.hotel.ui.fragments.login.hotel

interface LoginHotelListener {
    fun loginSuccess(hotelId: String)
    fun showHotelCode(hotelId: String, hotelCode: String)
}