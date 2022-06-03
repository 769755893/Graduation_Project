package com.app.project.hotel.ui.view.dialog

import android.content.Context
import com.app.project.hotel.R
import com.app.project.hotel.databinding.*
import com.app.project.hotel.common.BaseFragment
import com.jakewharton.rxbinding3.view.clicks

fun BaseFragment<FragmentHotelMainPageBinding>.showSuccessDialog(outSideCancelable: Boolean) {
    val dialog = object : BaseDialog<DialogHotelRoomUploadSuccessBinding>(
        requireContext(),
        R.layout.dialog_hotel_room_upload_success, outSideCancelable
    ) {}
    dialog.binding.apply {
        this.btnConfirm.clicks()
            .subscribe {
                dialog.dismiss()
            }.bindLife()
    }
    dialog.show()
}

fun BaseFragment<FragmentHotelMainPageBinding>.showFailedDialog(
    msg: String = "",
    outSideCancelable: Boolean
) {
    val dialog = object : BaseDialog<DialogHotelRoomUploadFailedBinding>(
        requireContext(),
        R.layout.dialog_hotel_room_upload_failed,
        outSideCancelable
    ) {}
    dialog.binding.apply {
        this.btnConfirm.clicks()
            .subscribe {
                dialog.dismiss()
            }.bindLife()
        if (msg.isNotBlank()) {
            this.textView.text = msg
        }
    }
    dialog.show()
}


fun BaseFragment<FragmentHotelProfileBinding>.showSuccessDialog(outSideCancelable: Boolean): BaseDialog<DialogHotelUpdateMsgSuccessBinding> {
    val dialog = object : BaseDialog<DialogHotelUpdateMsgSuccessBinding>(
        requireContext(),
        R.layout.dialog_hotel_update_msg_success, outSideCancelable
    ) {}
    dialog.binding.apply {
        this.btnConfirm.clicks()
            .subscribe {
                dialog.dismiss()
            }.bindLife()
    }
    return dialog
}

fun BaseFragment<FragmentUserProFileBinding>.showUpdateSuccessMsg(outSideCancelable: Boolean): BaseDialog<DialogHotelUpdateMsgSuccessBinding> {
    val dialog = object : BaseDialog<DialogHotelUpdateMsgSuccessBinding>(
        requireContext(),
        R.layout.dialog_hotel_update_msg_success, outSideCancelable
    ) {}
    dialog.binding.apply {
        this.btnConfirm.clicks()
            .subscribe {
                dialog.dismiss()
            }.bindLife()
    }
    return dialog
}

fun ShowNetWorkConnetFailedDialog(context: Context) {
    val dialog = object : BaseDialog<DialogNetWorkConnetFailedBinding>(context, R.layout.dialog_net_work_connet_failed, true){}
    dialog.binding.apply {
        this.btnConfirm.setOnClickListener {
            dialog.dismiss()
        }
    }
    dialog.show()
}