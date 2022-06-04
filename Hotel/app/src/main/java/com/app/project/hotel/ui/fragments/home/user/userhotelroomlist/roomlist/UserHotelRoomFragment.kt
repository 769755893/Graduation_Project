package com.app.project.hotel.ui.fragments.home.user.userhotelroomlist.roomlist

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.drawable.ColorDrawable
import android.media.tv.TvView
import android.os.Build
import android.view.InputEvent
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.widget.AbsListView
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.app.project.hotel.ui.view.dialog.showProgressDialogL
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
            viewBind.slRefreshLayout.isRefreshing = false
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

    var startRawY = 0F
    var startY = 0F
    @RequiresApi(Build.VERSION_CODES.M)
    override fun initCaseSecond() {
        viewBind.slRefreshLayout.setOnRefreshListener {
            viewModel.CURRENT_HOTEL_ID = ""
            viewModel.initHotelRoomData(viewModel.KEPP_HOTEL_ID_TEMPLY, showProgressDialogL())
        }

        viewBind.rv.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                v ?: return false
                event ?: return false

                val tracker = VelocityTracker.obtain()
                tracker.addMovement(event)
                tracker.computeCurrentVelocity(1000)

                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        startY = event.y
                        startRawY = event.rawY
                    }
                    MotionEvent.ACTION_UP -> {
                       val judgeY = (event.y - startY)
                        if (judgeY < -15) {
                            viewBind.tvStartTime.setTextColor(ResourcesCompat.getColor(resources, R.color.white, resources.newTheme()))
                            viewBind.tvEndTime.setTextColor(ResourcesCompat.getColor(resources, R.color.white, resources.newTheme()))
                            viewBind.tvEndTime.setHintTextColor(ResourcesCompat.getColor(resources, R.color.white, resources.newTheme()))
                            viewBind.tvZhi.setTextColor(ResourcesCompat.getColor(resources, R.color.white, resources.newTheme()))
                            viewBind.tvDayCount.setTextColor(ResourcesCompat.getColor(resources, R.color.white, resources.newTheme()))
                            viewBind.tvCommentHint.setTextColor(ResourcesCompat.getColor(resources, R.color.white, resources.newTheme()))
                        } else if (judgeY > 15) {
                            viewBind.tvStartTime.setTextColor(ResourcesCompat.getColor(resources, R.color.primary, resources.newTheme()))
                            viewBind.tvEndTime.setTextColor(ResourcesCompat.getColor(resources, R.color.primary, resources.newTheme()))
                            viewBind.tvEndTime.setHintTextColor(ResourcesCompat.getColor(resources, R.color.primary, resources.newTheme()))
                            viewBind.tvZhi.setTextColor(ResourcesCompat.getColor(resources, R.color.primary, resources.newTheme()))
                            viewBind.tvDayCount.setTextColor(ResourcesCompat.getColor(resources, R.color.primary, resources.newTheme()))
                            viewBind.tvCommentHint.setTextColor(ResourcesCompat.getColor(resources, R.color.primary, resources.newTheme()))
                        }
                    }
                    MotionEvent.ACTION_MOVE -> {
                        tracker.yVelocity.log()
                    }
                }

                return false
            }

        })
    }

    override fun provideLayoutId() = R.layout.fragment_user_room_list

}