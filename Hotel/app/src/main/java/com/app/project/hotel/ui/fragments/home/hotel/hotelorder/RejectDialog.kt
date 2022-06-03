package com.app.project.hotel.ui.fragments.home.hotel.hotelorder

import com.app.project.hotel.databinding.DialogHotelRejectCaseBinding
import com.app.project.hotel.ui.view.dialog.BaseDialog
import com.app.project.hotel.R
import com.app.project.hotel.ui.view.dialog.showHotelOrderCancelProgress
import com.jakewharton.rxbinding3.view.clicks

fun HotelOrderFragment.showRejectDialog(orderId: String, hotelId: Int, position: Int) {
    val dialog = object : BaseDialog<DialogHotelRejectCaseBinding>(
        requireContext(),
        R.layout.dialog_hotel_reject_case,
        false
    ) {}
    dialog.show()
    dialog.binding.btnCommit.clicks()
        .subscribe {
            dialog.dismiss()
            val dialogProgress = showHotelOrderCancelProgress()
            viewModel.hotelCancel(orderId, hotelId, dialog.binding.reason, context, dialogProgress, position)
        }.bindLife()
}