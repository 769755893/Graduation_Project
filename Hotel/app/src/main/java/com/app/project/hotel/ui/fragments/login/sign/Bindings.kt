package com.app.project.hotel.ui.fragments.login.sign

import android.widget.EditText
import androidx.databinding.BindingAdapter
import com.app.project.hotel.R
import com.example.uitraning.util.Utils


@BindingAdapter("RightDrawable")
fun setEditTextDrawable (view: EditText, RightDrawable: Boolean) {
    if (RightDrawable) {
        val drawable = Utils.bitmapToDrawable(R.drawable.icn_chevron_down_black,view,100F,60F)
        view.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,drawable,null)
    }
}