package com.app.project.hotel.ui.fragments.login.hotel

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.project.hotel.R
import com.app.project.hotel.databinding.FragmentLoginHotelBinding
import com.app.project.hotel.ui.activity.MainActivity
import com.app.project.hotel.ui.activity.showNotUserDialog
import com.app.project.hotel.common.BaseFragment
import com.app.project.hotel.ui.fragments.login.main.showProgressDialog
import com.app.project.hotel.ui.view.dialog.showHotelCodeDialog
import com.jakewharton.rxbinding3.view.clicks
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginHotelFragment : BaseFragment<FragmentLoginHotelBinding>() {
    private val viewModel by viewModels<LoginHotelViewModel> ()
    override fun onStart() {
        super.onStart()

        (activity as MainActivity).apply {
            leftScroll = {
                this.showNotUserDialog("超级用户")
            }
            rightScroll ={
                findNavController().navigate(R.id.action_login_Hotel_to_loginMain)
            }
            bottomBarVisibility.invoke(View.VISIBLE)
        }
    }

    override fun onStop() {
        super.onStop()
        (activity as MainActivity).apply {
            touchClear()
        }
    }


    override fun provideLayoutId() = R.layout.fragment_login_hotel
    override fun initCase() {
        viewBind.apply {
            btnLogin.clicks()
                .subscribe {
                    viewModel.login(this, showProgressDialog())
                }.bindLife()
            btnSign.clicks()
                .subscribe {
                    viewModel.sign(this)
                }.bindLife()
            viewModel.data.observe(this@LoginHotelFragment) {
                viewBind.data = it
            }
            backBtn.clicks()
                .subscribe {
                    findNavController().navigate(R.id.action_login_Hotel_to_loginMain)
                }.bindLife()
        }

        viewModel.callback = object : LoginHotelListener {
            override fun loginSuccess(hotelId: String) {
                val activity = activity as MainActivity
                activity.bottomBarVisibility.invoke(View.GONE)
                val bundle = Bundle()
                bundle.putString("hotelId",hotelId)
                findNavController().navigate(R.id.action_login_Hotel_to_hotelMainPage, bundle)
            }

            override fun showHotelCode(hotelId: String, hotelCode: String) {
                showHotelCodeDialog(requireContext(), hotelId, hotelCode, R.layout.dialog_hotel_sign_success, true)
            }
        }
    }

}