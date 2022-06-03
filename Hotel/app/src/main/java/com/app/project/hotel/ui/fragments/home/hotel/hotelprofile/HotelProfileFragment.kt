package com.app.project.hotel.ui.fragments.home.hotel.hotelprofile

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.activityViewModels
import com.app.project.hotel.R
import com.app.project.hotel.databinding.FragmentHotelProfileBinding
import com.app.project.hotel.common.BaseFragment
import com.app.project.hotel.ui.fragments.home.hotel.HotelMainPageViewModel
import com.app.project.hotel.ui.view.dialog.showSuccessDialog
import com.app.project.hotel.ui.view.dialog.showUpLoadHotelIconDialog
import com.example.uitraning.util.Utils
import com.github.gzuliyujiang.imagepicker.ImagePicker
import com.github.gzuliyujiang.imagepicker.PickCallback
import com.jakewharton.rxbinding3.view.clicks
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HotelProfileFragment: BaseFragment<FragmentHotelProfileBinding>() {
    private val viewModel by activityViewModels<HotelMainPageViewModel>()
    private val picker = ImagePicker.getInstance()
    override fun provideLayoutId(): Int {
        return R.layout.fragment_hotel_profile
    }

    override fun initCase() {
        viewModel.data.observe(this) {
            viewBind.data = it
        }

        viewBind.ivHotelIcon.clicks()
            .subscribe {
                picker.startGallery(this, true, object : PickCallback() {
                    override fun onPickImage(imageUri: Uri?) {
                        var bitmap = Utils.getBitmapFromUri(imageUri!!, requireContext())
                        bitmap = Utils.bitmapResizeFloat(bitmap!!, 0.7f,0.7f)
                        viewBind.ivHotelIcon.background = ColorDrawable(Color.TRANSPARENT)
                        viewBind.ivHotelIcon.setImageBitmap(bitmap)
                        viewModel.data.value?.hotelIcon = Utils.bitmap2String(bitmap)
                    }
                })
            }.bindLife()

        viewBind.tvUpdateIconBtn.clicks()
            .subscribe {
                val dialog = showUpLoadHotelIconDialog()
                val sd = showSuccessDialog(false)
                viewModel.upLoadHotelIcon(viewBind, dialog, sd)

            }.bindLife()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        picker.onActivityResult(this, requestCode, resultCode, data)
    }
}