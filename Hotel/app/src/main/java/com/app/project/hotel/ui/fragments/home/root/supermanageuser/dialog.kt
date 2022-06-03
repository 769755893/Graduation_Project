package com.app.project.hotel.ui.fragments.home.root.supermanageuser

import android.app.ProgressDialog

fun SuperManageUserFragment.showUpLoadDialog(): ProgressDialog {
    val dialog = ProgressDialog(requireContext())
    dialog.setTitle("更新中")
    dialog.show()
    return dialog
}

fun SuperManageUserFragment.showDownLoadProcess(): ProgressDialog {
    val dialog = object : ProgressDialog(requireContext()) {}
    dialog.setTitle("加载中，请稍后")
    dialog.setCanceledOnTouchOutside(false)
    dialog.show()
    return dialog
}