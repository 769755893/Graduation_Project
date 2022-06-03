package com.app.project.hotel.ui.fragments.home.user.main

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.app.project.hotel.R
import com.app.project.hotel.databinding.FragmentUserMainPageBinding
import com.app.project.hotel.common.BaseFragment
import com.app.project.hotel.common.LoginState
import com.app.project.hotel.ui.activity.MainActivity
import com.app.project.hotel.ui.fragments.home.user.userhotellist.UserHotelListFragment
import com.app.project.hotel.ui.fragments.home.user.userhotellist.UserHotelListViewModel
import com.app.project.hotel.ui.fragments.home.user.userorderlist.UserOrderListFragment
import com.app.project.hotel.ui.fragments.home.user.userprofile.ProFileFragment
import com.app.project.hotel.ui.fragments.home.user.userprofile.ProFileViewModel
import com.app.project.hotel.util.helper.ZoomOutPageTransformer
import com.example.uitraning.util.Utils
import com.jakewharton.rxbinding3.view.clicks
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserMainPage : BaseFragment<FragmentUserMainPageBinding>() {
    private val viewModel by activityViewModels<ProFileViewModel>()

    private fun setUpQuitAccount() {
        AlertDialog.Builder(requireContext())
            .setMessage("是否退出当前账号?")
            .setCancelable(true)
            .setPositiveButton(
                "是"
            ) { dialog, _ ->
                dialog?.dismiss()
                LoginState.loginState = false
                viewModel.firstTime = true
                findNavController().navigate(R.id.action_userMainPage_to_loginMain)
            }
            .setNegativeButton(
                "否"
            ) { dialog, _ -> dialog?.dismiss() }
            .show()
    }

    override fun provideLayoutId() = R.layout.fragment_user_main_page

    override fun initCaseSecond() {
        super.initCaseSecond()
        viewBind.ivSupport.clicks()
            .subscribe {
                findNavController().navigate(R.id.support)
            }.bindLife()
    }

    override fun initCase() {
        val userId = arguments?.getString("userId")!!
        viewModel.initUserData(userId)
        val fragments = listOf(UserHotelListFragment(), UserOrderListFragment(), ProFileFragment())
        Utils.changedViewPagerSensitive(viewBind.vp)

        viewBind.vp.adapter = ViewPagerAdapter(fragments, childFragmentManager, lifecycle)
        viewBind.vp.setPageTransformer(ZoomOutPageTransformer())
        viewBind.backBtn.clicks()
            .doOnNext {
                setUpQuitAccount()
            }.bindLife()
        viewBind.btnProfile.setOnClickListener {
            viewBind.vp.currentItem = 2
            viewBind.position = -1
        }
        viewBind.btnOrder.setOnClickListener {
            viewBind.vp.currentItem = 1
            viewBind.position = -1
        }
        viewBind.btnHome.setOnClickListener {
            viewBind.vp.currentItem = 0
            viewBind.position = 0
        }

        viewBind.vp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewBind.position = position
            }
        })
    }

}