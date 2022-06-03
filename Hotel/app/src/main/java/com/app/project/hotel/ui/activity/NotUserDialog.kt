package com.app.project.hotel.ui.activity

import android.app.ProgressDialog
import android.widget.Toast
import com.app.project.hotel.R
import com.app.project.hotel.databinding.DialogNotNormalUserEnterBinding
import com.app.project.hotel.ui.view.dialog.BaseDialog
import com.jakewharton.rxbinding3.view.clicks

fun MainActivity.showNotUserDialog(hint: String) {
    val dialog = object : BaseDialog<DialogNotNormalUserEnterBinding>(this, R.layout.dialog_not_normal_user_enter, false){}
    dialog.binding.hint = "请输入${hint}功能的授权码"
    dialog.show()
    dialog.binding.btnCommit.clicks().subscribe {
        if (!dialog.binding.key.isNullOrEmpty()) {
            if (hint == "酒店用户") {
                dialog.dismiss()
                viewModel.hotelEnterRequest(key = dialog.binding.key, dialog = showProgressDialog())
                changeIndex.invoke(1)
            }
            else {
                dialog.dismiss()
                viewModel.superEnterRequest(key = dialog.binding.key, dialog = showProgressDialog())
                changeIndex.invoke(2)
            }
        } else {
            Toast.makeText(this, "Key 不能为空", Toast.LENGTH_SHORT).show()
        }
    }.bindLife()
}

fun MainActivity.showProgressDialog(): ProgressDialog {
    val dialog = ProgressDialog(this)
    dialog.setTitle("响应中，请稍后")
    dialog.show()
    return dialog
}