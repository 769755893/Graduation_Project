package com.app.project.hotel.ui.fragments.home.user.userhotelroomlist

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.app.project.hotel.R
import com.app.project.hotel.databinding.FragmentUserHotelRoomPageBinding
import com.app.project.hotel.common.BaseFragment
import com.app.project.hotel.ui.activity.MainActivity
import com.app.project.hotel.ui.fragments.home.hotel.HotelMainPageViewModel
import com.app.project.hotel.ui.fragments.home.user.userhotellist.UserHotelListViewModel
import com.app.project.hotel.ui.fragments.home.user.userhotelroomlist.comments.HotelCommentFragment
import com.app.project.hotel.ui.fragments.home.user.userhotelroomlist.roomlist.UserHotelRoomFragment
import com.app.project.hotel.ui.view.dialog.showProgressDialog
import com.app.project.hotel.util.helper.ZoomOutPageTransformer
import com.example.uitraning.util.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserHotelRoomRootFragment : BaseFragment<FragmentUserHotelRoomPageBinding>() {
    val viewModel by activityViewModels<UserHotelRoomViewModel>()
    val hotelViewModel by activityViewModels<HotelMainPageViewModel>()
    val hotelListViewModel by activityViewModels<UserHotelListViewModel>()


    override fun provideLayoutId(): Int {
        return R.layout.fragment_user_hotel_room_page
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("NotifyDataSetChanged")
    override fun initCase() {
        val hotelId = requireArguments().getString("hotelId")!!
        hotelViewModel.initHotelMsg(hotelId)
        viewModel.showDialog = {
            showProgressDialog()
        }
        viewModel.initHotelRoomData(hotelId)
        viewModel.initHotelCommentData(hotelId) //初始的数据共享给其他fragment使用时，Root页面也使用activityViewModels
        hotelListViewModel.initRoomPageIconAndName(hotelId)
        val fragments = listOf(UserHotelRoomFragment(), HotelCommentFragment())
        viewBind.vp.adapter = UserHotelRoomVpAdapter(fragments, lifecycle, childFragmentManager)
        viewBind.vp.setPageTransformer(ZoomOutPageTransformer())
        viewBind.vp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
            }
        })
        Utils.changedViewPagerSensitive(viewBind.vp)
    }
}