package com.app.project.hotel.ui.fragments.home.user.userorderlist

import android.app.ProgressDialog
import com.app.project.hotel.ui.fragments.home.user.userprofile.passorder.UserPassOrderFragment

fun UserOrderListFragment.showProgressDialog(): ProgressDialog {
    val dialog = object : ProgressDialog(requireContext()) {}
    dialog.setTitle("加载订单信息中，请稍后")
    dialog.setCanceledOnTouchOutside(false)
    dialog.show()
    return dialog
}

fun UserPassOrderFragment.showProgress(): ProgressDialog {
    val dialog = object : ProgressDialog(requireContext()) {}
    dialog.setTitle("加载订单信息中，请稍后")
    dialog.setCanceledOnTouchOutside(false)
    dialog.show()
    return dialog
}