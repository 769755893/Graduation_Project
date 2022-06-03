package com.app.project.hotel.ui.fragments.home.hotel.hotelmanage

import com.app.project.hotel.databinding.ItemHotelManageBinding

interface HotelManageListener {
    fun pickImage(binding: ItemHotelManageBinding, position: Int)
    fun notifyInputMsgWeak()
}