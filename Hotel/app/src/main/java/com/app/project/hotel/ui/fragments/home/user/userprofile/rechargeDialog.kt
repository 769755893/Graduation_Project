package com.app.project.hotel.ui.fragments.home.user.userprofile

import android.widget.Toast
import com.app.project.hotel.R
import com.app.project.hotel.databinding.DialogUserRechargeBinding
import com.app.project.hotel.ui.view.dialog.BaseDialog
import com.jakewharton.rxbinding3.view.clicks

fun ProFileFragment.showRechargeDialog() {
    val dialog = object : BaseDialog<DialogUserRechargeBinding>(
        requireContext(),
        R.layout.dialog_user_recharge,
        true
    ) {}
    dialog.binding.btnSubmit.clicks()
        .subscribe {
            if (dialog.binding.rechargeKey.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "密匙不能为空~", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.userRecharge(dialog.binding!!.rechargeKey!!)
                dialog.dismiss()
            }
        }.bindLife()

    dialog.binding.btnBack.clicks()
        .subscribe {
            dialog.dismiss()
        }.bindLife()
    dialog.show()
}