package com.app.project.hotel.ui.fragments.home.user.userprofile.passorder.commentdialog

import android.graphics.Color
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import com.app.project.hotel.R
import com.app.project.hotel.base.responsmodel.UserOrderListDataModel
import com.app.project.hotel.databinding.DialogUserCommentBinding
import com.app.project.hotel.ui.fragments.home.user.userprofile.passorder.UserPassOrderFragment
import com.app.project.hotel.ui.view.dialog.BaseDialog
import com.app.project.hotel.ui.view.dialog.showUpLoadProcess
import com.example.uitraning.util.Utils
import com.github.gzuliyujiang.wheelpicker.OptionPicker
import com.jakewharton.rxbinding3.view.clicks

@RequiresApi(Build.VERSION_CODES.O)
fun UserPassOrderFragment.showPassOrderCommentDialog(data: UserOrderListDataModel.Data, position: Int) {
    val dialog = object : BaseDialog<DialogUserCommentBinding>(
        requireContext(),
        R.layout.dialog_user_comment,
        true
    ) {}
    val st = data.startTime
    val et = data.endTime
    val daycnt = et!!.split('-')[2].toInt() - st!!.split('-')[2].toInt() + 1
    dialog.binding.dayCount = daycnt
    dialog.binding.data = data
    dialog.show()

    val optionPicker = OptionPicker(requireActivity()).apply {
        this.setData(listOf(1, 2, 3, 4, 5))
        this.setCanceledOnTouchOutside(false)
        this.setOnOptionPickedListener { position, item ->
            dialog.binding.tvScoreTemp.text = item.toString()
            data.userCommentScore = item.toString().toInt()
        }
        this.cancelView.apply {
            this.visibility = View.GONE
        }
        this.titleView.apply {
            this.layoutParams.height = Utils.dpToPixels(44, context).toInt()
        }
        this.contentView.apply {
            this.layoutParams.height = Utils.dpToPixels(225, context).toInt()
        }
        this.wheelLayout.apply {
            this.setVisibleItemCount(3)
            this.setIndicatorColor(Color.rgb(0xC8, 0xC7, 0xCC))
            this.setIndicatorSize(2F)
            this.setCurvedIndicatorSpace(100)
        }
        this.okView.apply {
            this.text = "完了"
            this.setTextColor(Color.rgb(0xF5, 0xA6, 0x23))
        }
    }

    dialog.binding.cvCardScore.clicks()
        .subscribe {
            optionPicker.show()
        }.bindLife()

    dialog.binding.btnCommit.clicks()
        .subscribe {
            viewModel.upLoadUserComment(data, context, showUpLoadProcess())
            dialog.dismiss()
            viewModel.refreshUI?.invoke(position, 4)
        }.bindLife()
}