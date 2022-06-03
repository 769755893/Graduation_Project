package com.app.project.hotel.ui.fragments.home.user.userorderlist

import com.app.project.hotel.R
import com.app.project.hotel.base.responsmodel.UserOrderListDataModel
import com.app.project.hotel.databinding.DialogUserOrderBeRejectReasonBinding
import com.app.project.hotel.ui.view.dialog.BaseDialog
import com.example.uitraning.util.log
import com.jakewharton.rxbinding3.view.clicks

fun UserOrderListFragment.showReasonDialog(
    orderId: String?,
    reason: String?,
    hotelName: String?,
    hotelPhone: String?
) {
    val dialog = object : BaseDialog<DialogUserOrderBeRejectReasonBinding>(requireContext(), R.layout.dialog_user_order_be_reject_reason, true) {}
    dialog.binding.reason = reason
    dialog.binding.hotelName = hotelName
    dialog.binding.hotelPhone = hotelPhone
    dialog.show()
    dialog.binding.btnConfirm.clicks()
        .subscribe {
            dialog.dismiss()
            viewModel.userOrderConfirm(orderId!!)
        }.bindLife()
}