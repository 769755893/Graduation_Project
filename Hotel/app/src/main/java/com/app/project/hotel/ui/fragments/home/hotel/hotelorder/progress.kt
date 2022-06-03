package com.app.project.hotel.ui.fragments.home.hotel.hotelorder

import android.app.ProgressDialog

fun HotelOrderFragment.showProgressDialog(): ProgressDialog {
    val dialog = object : ProgressDialog(requireContext()) {}
    dialog.setTitle("加载中，请稍后")
    dialog.setCanceledOnTouchOutside(false)
    dialog.show()
    return dialog
}