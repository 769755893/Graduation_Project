package com.app.project.hotel.ui.fragments.home.hotel

import android.app.ProgressDialog

fun HotelMainPageFragment.showProgressDialog(): ProgressDialog {
    val dialog = object : ProgressDialog(requireContext()) {}
    dialog.setTitle("加载中，请稍后")
    dialog.setCanceledOnTouchOutside(false)
    dialog.show()
    return dialog
}