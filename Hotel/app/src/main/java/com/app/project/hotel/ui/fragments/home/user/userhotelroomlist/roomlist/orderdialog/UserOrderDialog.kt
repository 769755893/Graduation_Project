package com.app.project.hotel.ui.fragments.home.user.userhotelroomlist.roomlist.orderdialog

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Build
import android.view.Gravity
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import com.app.project.hotel.R
import com.app.project.hotel.databinding.DialogOrderConfigmBinding
import com.app.project.hotel.databinding.DialogRoomOrderBinding
import com.app.project.hotel.ui.fragments.home.user.userhotelroomlist.roomlist.UserHotelRoomFragment
import com.app.project.hotel.ui.view.dialog.BaseDialog
import com.example.uitraning.util.coroutines.Main
import com.jakewharton.rxbinding3.view.clicks

@RequiresApi(Build.VERSION_CODES.O)
fun UserHotelRoomFragment.showOrderDialog(dataUser: UserOrderData, dayCount: Int) {
    val dialog = object :
        BaseDialog<DialogRoomOrderBinding>(requireContext(), R.layout.dialog_room_order, true) {}

    dialog.binding.data = dataUser
    dialog.binding.data!!.roomPrice =
        (dayCount * dialog.binding.data?.roomPrice!!.toInt()).toString()
    dialog.binding.dayCount = dayCount

    val lp = FrameLayout.LayoutParams(dialog.binding.root.layoutParams)
    lp.gravity = Gravity.BOTTOM
    dialog.binding.root.layoutParams = lp

    dialog.binding.btnOrderConfirm.clicks()
        .subscribe {
            dialog.dismiss()
            showOrderConfirmDialog(dataUser)
        }.bindLife()

    dialog.base.root.setOnClickListener {
        dialog.dismiss()
    }

    dialog.show()
}


@RequiresApi(Build.VERSION_CODES.O)
fun UserHotelRoomFragment.showOrderConfirmDialog(dataUser: UserOrderData) {
    val dialog = object : BaseDialog<DialogOrderConfigmBinding>(
        requireContext(),
        R.layout.dialog_order_configm,
        false
    ) {}
    dialog.binding.btnConfirm.clicks()
        .subscribe {
            userViewModel.userPay(dataUser.roomPrice!!.toInt())
            userViewModel.userPayState = {
                if (it) {
                    viewModel.orderPay(dataUser)
                    dialog.dismiss()
                    Main {
                        AlertDialog.Builder(requireContext())
                            .setMessage("已成功预订,在酒店确认前您可以取消订单")
                            .setIcon(R.drawable.ic_baseline_check_24)
                            .setCancelable(true)
                            .setPositiveButton("Confirm", object : DialogInterface.OnClickListener {
                                override fun onClick(p0: DialogInterface?, p1: Int) {
                                    p0?.dismiss()
                                }
                            })
                            .show()
                    }
                } else {
                    dialog.dismiss()
                    Main {
                        AlertDialog.Builder(requireContext())
                            .setMessage("非常抱歉，您的余额不足，请联系管理员获取充值密匙进行充值~")
                            .setIcon(R.drawable.ic_baseline_error_24)
                            .setCancelable(true)
                            .setPositiveButton("Confirm", object : DialogInterface.OnClickListener {
                                override fun onClick(p0: DialogInterface?, p1: Int) {
                                    p0?.dismiss()
                                }
                            })
                            .show()
                    }
                }
            }
        }.bindLife()
    dialog.binding.btnCancel.clicks()
        .subscribe {
            dialog.dismiss()
        }.bindLife()

    dialog.show()
}