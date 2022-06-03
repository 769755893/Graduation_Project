package com.app.project.hotel.ui.fragments.login.main

import android.graphics.drawable.BitmapDrawable
import androidx.databinding.BindingAdapter
import com.app.project.hotel.R
import com.app.project.hotel.ui.view.EditTextDrawableView
import com.example.uitraning.util.Utils

@BindingAdapter("resizeDrawable")
fun changed(view: EditTextDrawableView, resizeDrawable: Boolean) {
    val size = 0.8f
    if (resizeDrawable) {
        val bitmap = Utils.bitmapMarixScale(view.resources, R.drawable.bg_eye_slash_fill, size, size, view.context)
        val drawable = BitmapDrawable(bitmap)
        view.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawable, null)
    } else {
        val bitmap = Utils.bitmapMarixScale(view.resources, R.drawable.bg_eye_fill, size, size, view.context)
        val drawable = BitmapDrawable(bitmap)
        view.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawable, null)
    }
}