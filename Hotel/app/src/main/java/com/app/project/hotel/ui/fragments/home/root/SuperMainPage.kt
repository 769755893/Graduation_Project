package com.app.project.hotel.ui.fragments.home.root

import androidx.navigation.fragment.findNavController
import com.app.project.hotel.R
import com.app.project.hotel.databinding.FragmentSuperMainPageBinding
import com.app.project.hotel.common.BaseFragment
import com.app.project.hotel.ui.fragments.home.root.supermanagehotel.SuperManageHotelFragment
import com.app.project.hotel.ui.fragments.home.root.supermanageuser.SuperManageUserFragment
import com.app.project.hotel.ui.fragments.home.user.main.ViewPagerAdapter
import com.app.project.hotel.util.helper.ZoomOutPageTransformer
import com.example.uitraning.util.Utils

class SuperMainPage : BaseFragment<FragmentSuperMainPageBinding>() {

    override fun provideLayoutId() = R.layout.fragment_super_main_page

    override fun initCase() {
        val fragments = listOf(SuperManageUserFragment(), SuperManageHotelFragment())
        viewBind.vp.adapter = ViewPagerAdapter(fragments, parentFragmentManager,lifecycle)
        viewBind.vp.setPageTransformer(ZoomOutPageTransformer())
        Utils.changedViewPagerSensitive(viewBind.vp)
        viewBind.backBtn.setOnClickListener {
            findNavController().navigate(R.id.loginMain)
        }
        viewBind.btnOrder.setOnClickListener {
            viewBind.vp.currentItem = 1
        }
        viewBind.btnHome.setOnClickListener {
            viewBind.vp.currentItem = 0
        }
    }
}