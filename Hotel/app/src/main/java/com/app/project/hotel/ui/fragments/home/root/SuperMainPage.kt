package com.app.project.hotel.ui.fragments.home.root

import android.app.AlertDialog
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.app.project.hotel.R
import com.app.project.hotel.databinding.FragmentSuperMainPageBinding
import com.app.project.hotel.common.BaseFragment
import com.app.project.hotel.common.LoginState
import com.app.project.hotel.ui.activity.MainActivity
import com.app.project.hotel.ui.fragments.home.root.supermanagehotel.SuperManageHotelFragment
import com.app.project.hotel.ui.fragments.home.root.supermanageuser.SuperManageUserFragment
import com.app.project.hotel.ui.fragments.home.user.main.ViewPagerAdapter
import com.app.project.hotel.util.helper.ZoomOutPageTransformer
import com.example.uitraning.util.Utils

class SuperMainPage : BaseFragment<FragmentSuperMainPageBinding>() {

    override fun provideLayoutId() = R.layout.fragment_super_main_page

    private fun setUpQuitAccount() {
        AlertDialog.Builder(requireContext())
            .setMessage("是否退出?")
            .setCancelable(true)
            .setPositiveButton(
                "是"
            ) { dialog, _ ->
                dialog?.dismiss()
                findNavController().navigate(R.id.action_superMainPage_to_loginMain)
            }
            .setNegativeButton(
                "否"
            ) { dialog, _ -> dialog?.dismiss() }
            .show()
    }

    override fun initCase() {
        val fragments = listOf(SuperManageUserFragment(), SuperManageHotelFragment())
        viewBind.vp.adapter = ViewPagerAdapter(fragments, parentFragmentManager,lifecycle)
        viewBind.vp.setPageTransformer(ZoomOutPageTransformer())
        Utils.changedViewPagerSensitive(viewBind.vp)
        viewBind.backBtn.setOnClickListener {
            setUpQuitAccount()
        }
        viewBind.btnOrder.setOnClickListener {
            viewBind.vp.currentItem = 1
        }
        viewBind.btnHome.setOnClickListener {
            viewBind.vp.currentItem = 0
        }
    }



    override fun initCaseSecond() {
        super.initCaseSecond()
        (requireActivity() as MainActivity).addBackPressCallBack(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                setUpQuitAccount()
            }
        })
    }
}