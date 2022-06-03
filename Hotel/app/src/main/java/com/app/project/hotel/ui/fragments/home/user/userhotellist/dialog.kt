package com.app.project.hotel.ui.fragments.home.user.userhotellist

import android.app.ProgressDialog

fun UserHotelListFragment.showProgressDialog(): ProgressDialog {
    val dialog = ProgressDialog(requireContext())
    dialog.setTitle("数据加载中，请稍后")
    dialog.show()
    return dialog
}