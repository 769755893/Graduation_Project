package com.app.project.hotel.ui.fragments.home.hotel

import android.app.ProgressDialog
import android.view.WindowManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.app.project.hotel.R
import com.app.project.hotel.databinding.FragmentHotelMainPageBinding
import com.app.project.hotel.common.BaseFragment
import com.app.project.hotel.common.mwindow
import com.app.project.hotel.ui.fragments.home.user.main.ViewPagerAdapter
import com.app.project.hotel.ui.fragments.home.hotel.hotelmanage.HotelManageFragment
import com.app.project.hotel.ui.fragments.home.hotel.hotelorder.HotelOrderFragment
import com.app.project.hotel.ui.fragments.home.hotel.hotelprofile.HotelProfileFragment
import com.app.project.hotel.ui.view.dialog.showDownLoadProcessDialog
import com.app.project.hotel.ui.view.dialog.showFailedDialog
import com.app.project.hotel.ui.view.dialog.showSuccessDialog
import com.app.project.hotel.util.helper.ZoomOutPageTransformer
import com.example.uitraning.util.Utils
import com.example.uitraning.util.log
import com.example.uitraning.util.showToast
import com.jakewharton.rxbinding3.view.clicks
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HotelMainPageFragment : BaseFragment<FragmentHotelMainPageBinding>() {
    private val viewModel by activityViewModels<HotelMainPageViewModel>()
    override fun provideLayoutId(): Int {
        return R.layout.fragment_hotel_main_page
    }

    override fun initCase() {
        viewModel.dialogCall = {
            showProgressDialog()
        }
        viewBind.tvTitle.isSelected = true
        val fragments = listOf(HotelManageFragment(), HotelOrderFragment(), HotelProfileFragment())
        viewBind.vp.adapter = ViewPagerAdapter(fragments, parentFragmentManager, lifecycle)
        viewBind.vp.setPageTransformer(ZoomOutPageTransformer())
        viewBind.backBtn.setOnClickListener {
            findNavController().navigate(R.id.action_hotelMainPage_to_login_Hotel)
        }
        viewBind.btnHome.setOnClickListener {
            viewBind.vp.currentItem = 0
        }
        viewBind.btnOrder.setOnClickListener {
            viewBind.vp.currentItem = 1
        }
        viewBind.btnProfile.clicks().subscribe {
            viewBind.vp.currentItem = 2
        }.bindLife()
        Utils.changedViewPagerSensitive(viewBind.vp)
        initHotelMsg()
    }

    private fun initHotelMsg() {
        val hotelId = arguments?.getString("hotelId")
        hotelId?.log("此时Hotelid为")
        viewModel.initHotelMsg(hotelId!!)
        viewModel.data.observe(this) {
            viewBind.data = it
        }
        viewModel.callback = object : HotelMainPageListener {
            override fun showMsg(msg: String) {
                showToast(requireContext(), msg, mwindow)
            }

            override fun showDialog(msg: String) {
                when (msg) {
                    "UnKnown Error" -> {
                        showFailedDialog("", true)
                    }
                    "上传成功" -> {
                        showSuccessDialog(true)
                    }
                    else -> {
                        showFailedDialog(msg, true)
                    }
                }
            }
        }
    }
}
