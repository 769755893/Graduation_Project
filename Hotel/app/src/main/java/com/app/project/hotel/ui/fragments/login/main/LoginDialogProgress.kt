package com.app.project.hotel.ui.fragments.login.main

import android.app.ProgressDialog
import com.app.project.hotel.ui.fragments.login.hotel.LoginHotelFragment

fun LoginMainFragment.showProgressDialog(): ProgressDialog {
    val dialog = object : ProgressDialog(requireContext()) {}
    dialog.setTitle("请求中，请稍后")
    dialog.setCanceledOnTouchOutside(false)
    dialog.show()
    return dialog
}

fun LoginHotelFragment.showProgressDialog(): ProgressDialog {
    val dialog = object : ProgressDialog(requireContext()) {}
    dialog.setTitle("请求中，请稍后")
    dialog.setCanceledOnTouchOutside(false)
    dialog.show()
    return dialog
}