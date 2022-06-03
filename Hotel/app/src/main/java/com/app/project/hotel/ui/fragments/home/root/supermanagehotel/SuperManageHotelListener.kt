package com.app.project.hotel.ui.fragments.home.root.supermanagehotel

import com.app.project.hotel.databinding.ItemSuperManageHotelParentBinding
import com.app.project.hotel.databinding.ItemSuperManageRoomChildBinding

interface SuperManageHotelListener {
    fun displayRoomListClick(hotelId: String, position: Int)
    fun upLoadRoomStatus(roomId: String, i: Int, binding: ItemSuperManageRoomChildBinding)
    fun changeHotelState(binding: ItemSuperManageHotelParentBinding)
}