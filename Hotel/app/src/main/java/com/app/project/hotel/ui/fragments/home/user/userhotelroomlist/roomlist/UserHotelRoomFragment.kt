package com.app.project.hotel.ui.fragments.home.user.userhotelroomlist.roomlist

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.project.hotel.R
import com.app.project.hotel.databinding.FragmentUserRoomListBinding
import com.app.project.hotel.databinding.ItemUserHotelRoomBinding
import com.app.project.hotel.common.BaseFragment
import com.app.project.hotel.ui.activity.MainActivity
import com.app.project.hotel.ui.fragments.home.user.userhotellist.UserHotelListViewModel
import com.app.project.hotel.ui.fragments.home.user.userhotelroomlist.UserHotelRoomViewModel
import com.app.project.hotel.ui.fragments.home.user.userhotelroomlist.roomlist.orderdialog.UserOrderData
import com.app.project.hotel.ui.fragments.home.user.userhotelroomlist.roomlist.orderdialog.showOrderDialog
import com.app.project.hotel.ui.fragments.home.user.userprofile.ProFileViewModel
import com.app.project.hotel.ui.view.pickerview.showTimePicker
import com.example.uitraning.util.Utils
import com.example.uitraning.util.log
import com.jakewharton.rxbinding3.view.clicks
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserHotelRoomFragment() : BaseFragment<FragmentUserRoomListBinding>() {
    val viewModel by activityViewModels<UserHotelRoomViewModel>()
    val userViewModel by activityViewModels<ProFileViewModel>()
    val hotelListViewModel by activityViewModels<UserHotelListViewModel>()

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun initCase() {
        val adapter = UserHotelRoomAdapter()
        viewBind.rv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        viewBind.rv.adapter = adapter

        viewModel.data.observe(this) {
            adapter.data = it
            adapter.notifyDataSetChanged()
        }
        hotelListViewModel.roomPageData.observe(this) {
            viewBind.hotelName = it?.hotelName
            viewBind.hotelIcon = Utils.string2Bitmap(it?.hotelIcon)?.toDrawable(resources)
        }

        viewBind.tvStartTime.text = Utils.getNowTimeStringymd()
        viewBind.tvStartTime.clicks()
            .subscribe {
                showTimePicker(requireActivity(), viewBind, 1)
            }.bindLife()
        viewBind.tvEndTime.clicks()
            .subscribe {
                showTimePicker(requireActivity(), viewBind, 0)
            }.bindLife()
        adapter.callback = object : UserHotelRoomListener {
            override fun showOrderDialogStart(binding: ItemUserHotelRoomBinding?) {
                if (viewBind.startTime == null || viewBind.endTime == null || !Utils.checkTime(
                        viewBind.startTime.toString(),
                        viewBind.endTime.toString()
                    )
                ) {
                    AlertDialog.Builder(requireContext())
                        .setMessage("请确认您的入住开始和结束时间")
                        .setCancelable(true)
                        .setPositiveButton("Confirm", object : DialogInterface.OnClickListener {
                            override fun onClick(p0: DialogInterface?, p1: Int) {
                                p0!!.dismiss()
                            }
                        })
                        .show()
                } else {
                    var orderData = UserOrderData().apply {
                        this.starTime = viewBind.startTime
                        this.endTime = viewBind.endTime
                    }
                    orderData = userViewModel.initOrderData(orderData)
                    orderData.apply {
                        this.hotelId = hotelListViewModel.roomPageData.value?.hotelId
                        this.hotelName = hotelListViewModel.roomPageData.value?.hotelName
                        this.hotelLocation = hotelListViewModel.roomPageData.value?.hotelLocation
                    }

                    viewModel.data.value?.forEach {
                        if (it?.roomId == binding?.data?.roomId) {
                            orderData.apply {
                                this.roomId = it?.roomId
                                this.roomName = it?.roomName
                                this.roomDesc = it?.roomDesc
                                this.roomFeature = it?.roomFeature
                                this.roomPrice = it?.roomPrice
                            }
                        }
                    }
                    viewBind.dayCount?.toInt()?.let { showOrderDialog(orderData, it) }
                }
            }
        }
    }


    override fun initCaseSecond() {
        super.initCaseSecond()
        val activity = activity as MainActivity
        activity.touchY = {
            val color = 0xF5A623 + it
            color.log("此时的颜色")
            viewBind.tvStartTime.setTextColor(color.toInt())
            viewBind.tvEndTime.setTextColor(color.toInt())
        }
    }

    override fun provideLayoutId() = R.layout.fragment_user_room_list

}