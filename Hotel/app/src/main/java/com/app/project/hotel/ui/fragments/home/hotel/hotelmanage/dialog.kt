package com.app.project.hotel.ui.fragments.home.hotel.hotelmanage

import com.app.project.hotel.R
import com.app.project.hotel.databinding.DialogHotelConfrmReasonBinding
import com.app.project.hotel.databinding.DialogUserOrderBeRejectReasonBinding
import com.app.project.hotel.ui.view.dialog.BaseDialog
import com.jakewharton.rxbinding3.view.clicks

fun HotelManageFragment.showHotelConfirmDialog(roomId: String, roomName: String, roomReason: String) {
    val dialog = object : BaseDialog<DialogHotelConfrmReasonBinding>(requireContext(), R.layout.dialog_hotel_confrm_reason, false){}
    val bind = dialog.binding
    bind.reason = "非常抱歉你的房间:$roomName 因为: $roomReason 未通过审核~"
    dialog.show()
    dialog.binding.btnConfirm.clicks()
        .subscribe {
            dialog.dismiss()
            viewModel.hotelRoomConfirm(roomId)
        }.bindLife()
}
