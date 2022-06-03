package com.app.project.hotel.ui.view.dialog

import android.app.ProgressDialog
import com.app.project.hotel.databinding.*
import com.app.project.hotel.common.BaseFragment

fun BaseFragment<FragmentUserHotelRoomPageBinding>.showProgressDialog(): ProgressDialog {
    val dialog = object : ProgressDialog(requireContext()) {}
    dialog.setTitle("加载中，请稍后")
    dialog.setCanceledOnTouchOutside(false)
    dialog.show()
    return dialog
}

fun BaseFragment<FragmentHotelMainPageBinding>.showDownLoadProcessDialog(): ProgressDialog {
    val dialog = object : ProgressDialog(requireContext()) {}
    dialog.setTitle("加载中，请稍后")
    dialog.setCanceledOnTouchOutside(false)
    dialog.show()
    return dialog
}

fun BaseFragment<FragmentHotelManageBinding>.showUpLoadProcessDialog(): ProgressDialog {
    val dialog = object : ProgressDialog(requireContext()) {}
    dialog.setTitle("上传中，请稍后")
    dialog.setCanceledOnTouchOutside(false)
    dialog.show()
    return dialog
}

fun BaseFragment<FragmentHotelProfileBinding>.showUpLoadHotelIconDialog(): ProgressDialog {
    val dialog = object : ProgressDialog(requireContext()) {}
    dialog.setTitle("上传中，请稍后")
    dialog.setCanceledOnTouchOutside(false)
    dialog.show()
    return dialog
}

fun BaseFragment<FragmentUserProFileBinding>.showUpLoadUserIconDialog(): ProgressDialog {
    val dialog = object : ProgressDialog(requireContext()) {}
    dialog.setTitle("上传中，请稍后")
    dialog.setCanceledOnTouchOutside(false)
    dialog.show()
    return dialog
}

fun BaseFragment<FragmentUserOrderListBinding>.showCommentUpLoadProcess(): ProgressDialog {
    val dialog = object : ProgressDialog(requireContext()) {}
    dialog.setTitle("上传中，请稍后")
    dialog.setCanceledOnTouchOutside(false)
    dialog.show()
    return dialog
}

fun BaseFragment<FragmentUserPassOrderBinding>.showUpLoadProcess(): ProgressDialog {
    val dialog = object : ProgressDialog(requireContext()) {}
    dialog.setTitle("上传中，请稍后")
    dialog.setCanceledOnTouchOutside(false)
    dialog.show()
    return dialog
}

fun BaseFragment<FragmentSuperManageHotelBinding>.showDownLoadProcess(): ProgressDialog {
    val dialog = object : ProgressDialog(requireContext()) {}
    dialog.setTitle("加载中，请稍后")
    dialog.setCanceledOnTouchOutside(false)
    dialog.show()
    return dialog
}

fun BaseFragment<FragmentSuperManageHotelBinding>.showRoomDownLoadProcess(): ProgressDialog {
    val dialog = object : ProgressDialog(requireContext()) {}
    dialog.setTitle("加载最新房间数据中，请稍后")
    dialog.setCanceledOnTouchOutside(false)
    dialog.show()
    return dialog
}

fun BaseFragment<FragmentSuperManageHotelBinding>.showUpLoadStatusProgress(): ProgressDialog {
    val dialog = object : ProgressDialog(requireContext()) {}
    dialog.setTitle("更新数据中，请稍后")
    dialog.setCanceledOnTouchOutside(false)
    dialog.show()
    return dialog
}


fun BaseFragment<FragmentUserOrderListBinding>.showOrderCancelProgress(): ProgressDialog {
    val dialog = object : ProgressDialog(requireContext()) {}
    dialog.setTitle("取消订单中，请稍后")
    dialog.setCanceledOnTouchOutside(false)
    dialog.show()
    return dialog
}

fun BaseFragment<FragmentHotelOrderBinding>.showHotelOrderConfirmProgress(): ProgressDialog {
    val dialog = object : ProgressDialog(requireContext()) {}
    dialog.setTitle("订单确认中，请稍后")
    dialog.setCanceledOnTouchOutside(false)
    dialog.show()
    return dialog
}

fun BaseFragment<FragmentHotelOrderBinding>.showHotelOrderCancelProgress(): ProgressDialog {
    val dialog = object : ProgressDialog(requireContext()) {}
    dialog.setTitle("拒绝订单中，请稍后")
    dialog.setCanceledOnTouchOutside(false)
    dialog.show()
    return dialog
}

fun BaseFragment<FragmentHotelOrderBinding>.showHotelOrderDownProgress(): ProgressDialog {
    val dialog = object : ProgressDialog(requireContext()) {}
    dialog.setTitle("更新订单信息中，请稍后")
    dialog.setCanceledOnTouchOutside(false)
    dialog.show()
    return dialog
}

fun BaseFragment<FragmentSuperManageUserBinding>.showDownLoadUserListDiaolog(): ProgressDialog {
    val dialog = object : ProgressDialog(requireContext()) {}
    dialog.setTitle("加载用户列表中，请稍后")
    dialog.setCanceledOnTouchOutside(false)
    dialog.show()
    return dialog
}