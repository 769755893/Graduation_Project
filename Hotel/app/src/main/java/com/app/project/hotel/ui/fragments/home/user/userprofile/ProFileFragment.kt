package com.app.project.hotel.ui.fragments.home.user.userprofile


import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.widget.Toast
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.app.project.hotel.R
import com.app.project.hotel.databinding.FragmentUserProFileBinding
import com.app.project.hotel.common.BaseFragment
import com.app.project.hotel.ui.view.dialog.showUpLoadUserIconDialog
import com.app.project.hotel.ui.view.dialog.showUpdateSuccessMsg
import com.example.uitraning.util.Utils
import com.example.uitraning.util.coroutines.Main
import com.example.uitraning.util.log
import com.github.gzuliyujiang.imagepicker.ImagePicker
import com.github.gzuliyujiang.imagepicker.PickCallback
import com.jakewharton.rxbinding3.view.clicks
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProFileFragment : BaseFragment<FragmentUserProFileBinding>() {
    val viewModel by activityViewModels<ProFileViewModel>()
    private val picker = ImagePicker.getInstance()

    override fun provideLayoutId(): Int {
        return R.layout.fragment_user_pro_file
    }

    override fun initViewModelData() {
        super.initViewModelData()
        viewModel.getUserMoney()
        viewModel.userChargeState = {
            if (it) {
                Main {
                    Toast.makeText(requireContext(), "充值成功", Toast.LENGTH_SHORT).show()
                    viewModel.getUserMoney()
                }
            } else {
                Main {
                    Toast.makeText(requireContext(), "充值失败", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun initCase() {

        viewModel.data.observe(this) {
            viewBind.data = it
        }

        viewModel.userMoney.observe(this) {
            viewBind.userMoney = it ?: "0"
        }

        viewBind.tvUserMoney.clicks()
            .subscribe {
                showRechargeDialog()
            }.bindLife()

        viewBind.ivUserIcon.clicks().subscribe {
            picker.startGallery(this, false, object : PickCallback() {
                override fun onPickImage(imageUri: Uri?) {
                    var bitmap = Utils.getBitmapFromUri(imageUri!!, requireContext())!!
                    bitmap = Utils.bitmapResizeFloat(bitmap, 0.7f, 0.7f)
                    val rd = RoundedBitmapDrawableFactory.create(resources, bitmap)
                    rd.isCircular = true
                    rd.cornerRadius = 250f
                    viewBind.ivUserIcon.background = ColorDrawable(Color.TRANSPARENT)
                    viewBind.ivUserIcon.setImageDrawable(rd)
                    viewModel.data.value?.userIcon = Utils.bitmap2String(rd.toBitmap())
                }
            })
        }.bindLife()

        viewBind.btnPassOrder.clicks()
            .subscribe {
                findNavController().navigate(R.id.user_pass_order)
            }.bindLife()

        var dialog: ProgressDialog?
        viewBind.tvUpdateIconBtn.clicks().subscribe {
            dialog = showUpLoadUserIconDialog()
            val icon = viewBind.ivUserIcon.drawable
            var bitmapStr = ""
            if (icon != null) {
                bitmapStr = Utils.bitmap2String(icon.toBitmap())
            }
            viewModel.upLoadUserMsg(
                viewBind.data!!,
                bitmapStr,
                dialog!!,
                showUpdateSuccessMsg(true)
            )
        }.bindLife()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        picker.onActivityResult(requireActivity(), requestCode, resultCode, data)
    }
}

