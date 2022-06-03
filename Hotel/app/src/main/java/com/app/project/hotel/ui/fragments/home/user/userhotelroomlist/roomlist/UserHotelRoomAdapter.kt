package com.app.project.hotel.ui.fragments.home.user.userhotelroomlist.roomlist

import com.app.project.hotel.R
import com.app.project.hotel.base.responsmodel.UserHotelRoomDataModel
import com.app.project.hotel.databinding.ItemUserHotelRoomBinding
import com.app.project.hotel.common.BaseRecyclerAdapter

class UserHotelRoomAdapter : BaseRecyclerAdapter<ItemUserHotelRoomBinding>() {
    var data = mutableListOf<UserHotelRoomDataModel.Data?>()
    var callback: UserHotelRoomListener? = null

    override fun provideLayoutId(): Int {
        return R.layout.item_user_hotel_room
    }

    override fun initViewCase(binding: ItemUserHotelRoomBinding, position: Int) {
        binding.data = data[position]
        binding.btnOrderRoom.setOnClickListener {
            callback?.showOrderDialogStart(binding)
        }
    }

    override fun getItemCount() = data.size
}