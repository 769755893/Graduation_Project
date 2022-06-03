package com.app.project.hotel.ui.fragments.home.user.userhotellist

import android.annotation.SuppressLint
import com.app.project.hotel.R
import com.app.project.hotel.base.responsmodel.HotelListDataModel
import com.app.project.hotel.databinding.ItemUserHotelBinding
import com.app.project.hotel.common.BaseRecyclerAdapter
import com.example.uitraning.util.log

class UserHotelListAdapter : BaseRecyclerAdapter<ItemUserHotelBinding>() {
    var data = mutableListOf<HotelListDataModel.Data?>()
    var callback: UserHotelListListener? = null

    @SuppressLint("NotifyDataSetChanged")
    fun filterText(text: String) {
        data = data.filter {
            text in it?.hotelName.toString() || text in it?.hotelLocation.toString()
        }.toMutableList()
        notifyDataSetChanged()
    }

    override fun provideLayoutId() = R.layout.item_user_hotel

    override fun initViewCase(binding: ItemUserHotelBinding, position: Int) {
        binding.tvHotelName.isSelected = true
        binding.tvHotelDesc.isSelected = true
        binding.tvComment.isSelected = true
        binding.tvHotelLocation.isSelected = true
        binding.data = data[position]
        binding.root.setOnClickListener {
            callback?.goHotelRoomPage(binding.data?.hotelId!!)
        }
    }

    override fun getItemCount() = data.size
    @SuppressLint("NotifyDataSetChanged")
    fun flushData(p2: Int) {
        if (p2 == 1) {
            data.sortBy {
                it?.hotelMinPrice?.toInt()
            }
        } else if (p2 == 2) {
            data.sortByDescending {
                it?.hotelAvgScore
            }
        } else {
            data.sortByDescending {
                it?.salesCnt!!.toInt()
            }
        }
        notifyDataSetChanged()
    }
}
