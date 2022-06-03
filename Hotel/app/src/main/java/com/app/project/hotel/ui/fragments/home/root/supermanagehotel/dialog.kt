package com.app.project.hotel.ui.fragments.home.root.supermanagehotel

import com.app.project.hotel.R
import com.app.project.hotel.databinding.DialogHotelRejectCaseBinding
import com.app.project.hotel.databinding.ItemSuperManageRoomChildBinding
import com.app.project.hotel.ui.view.dialog.BaseDialog
import com.app.project.hotel.ui.view.dialog.showUpLoadStatusProgress
import com.jakewharton.rxbinding3.view.clicks

fun SuperManageHotelFragment.showReasonInputDialog(
    roomId: String,
    i: Int,
    binging: ItemSuperManageRoomChildBinding
) {
    val dialog = object : BaseDialog<DialogHotelRejectCaseBinding>(requireContext(), R.layout.dialog_hotel_reject_case, false){}
    val bind = dialog.binding
    bind.tvHint.text = "输入当前房间不通过的原因"
    bind.btnCommit.clicks().subscribe {
        viewModel.upLoadRoomStatus(roomId, i, showUpLoadStatusProgress(), binging, bind.reason)
        dialog.dismiss()
    }.bindLife()
    dialog.show()
}