package com.app.project.hotel.ui.view.dialog

import android.content.Context
import androidx.annotation.LayoutRes
import com.app.project.hotel.databinding.DialogHotelSignSuccessBinding
import com.app.project.hotel.databinding.FragmentLoginHotelBinding
import com.app.project.hotel.common.BaseFragment
import com.jakewharton.rxbinding3.view.clicks

fun BaseFragment<FragmentLoginHotelBinding>.showHotelCodeDialog(context: Context, hotel_id: String, hotel_code: String, @LayoutRes layoutId: Int, cancelable: Boolean) {
    val dialog = object : BaseDialog<DialogHotelSignSuccessBinding>(context, layoutId, cancelable){}
    dialog.binding.hotelIdText = hotel_id
    dialog.binding.hotelCodeText = hotel_code

    dialog.binding.apply {
        hotelIdText = hotel_id
        hotelCodeText = hotel_code
        btnConfirm.clicks()
            .doOnNext {
                dialog.dismiss()
            }.bindLife()
    }
    dialog.show()
}