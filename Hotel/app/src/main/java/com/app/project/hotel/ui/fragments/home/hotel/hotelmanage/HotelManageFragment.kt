package com.app.project.hotel.ui.fragments.home.hotel.hotelmanage

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.project.hotel.R
import com.app.project.hotel.base.responsmodel.HotelManageOverViewDataModel
import com.app.project.hotel.databinding.FragmentHotelManageBinding
import com.app.project.hotel.databinding.ItemHotelManageBinding
import com.app.project.hotel.common.BaseFragment
import com.app.project.hotel.common.mwindow
import com.app.project.hotel.ui.fragments.home.hotel.HotelMainPageViewModel
import com.app.project.hotel.ui.view.dialog.showUpLoadProcessDialog
import com.example.uitraning.util.showToast
import com.github.gzuliyujiang.imagepicker.ImagePicker
import com.github.gzuliyujiang.imagepicker.PickCallback
import com.jakewharton.rxbinding3.view.clicks
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HotelManageFragment : BaseFragment<FragmentHotelManageBinding>() {
    val viewModel by activityViewModels<HotelMainPageViewModel>()
    val picker = ImagePicker.getInstance()
    var dialog: ProgressDialog? = null

    override fun provideLayoutId(): Int {
        return R.layout.fragment_hotel_manage
    }

    override fun initCase() {
        viewBind.rv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val adapter = HotelManageAdapter().apply {
            this.upLoadRoomMsg = { it, position ->
                dialog = showUpLoadProcessDialog()
                viewModel.upLoadRoom(it, position, this, dialog!!)
            }
            this.callback = object : HotelManageListener {
                override fun pickImage(binding: ItemHotelManageBinding, position: Int) {
                    picker.startGallery(this@HotelManageFragment, true, object : PickCallback() {
                        override fun onPickImage(imageUri: Uri?) {
                            viewModel.keepPositionImage(
                                position,
                                imageUri,
                                requireContext(),
                                this@apply
                            )
                        }
                    })
                }

                override fun notifyInputMsgWeak() {
                    showToast(requireContext(), "请检查您要提交的房间信息", mwindow, false)
                }
            }
        }
        viewBind.rv.adapter = adapter
        viewBind.btnAdd.clicks()
            .subscribe {
                if (adapter.data.size == 0) {
                    if (viewBind.rv.childCount > 0) {
                        viewBind.rv.removeAllViews()
                    }
                }
                adapter.addRoom()
            }.bindLife()
        initViewModel(adapter)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initViewModel(adapter: HotelManageAdapter) {
        viewModel.roomListData.value = null
        viewModel.roomListData.observe(this) { data ->
            if (data != null) {
                data.forEach {
                    if (it?.roomReason != null) {
                        if (it.roomReason!!.isNotEmpty() && it.hotelConfirm == 0) {
                            it.hotelConfirm = 1
                            showHotelConfirmDialog(it.roomId!!, it.roomName!!, it.roomReason!!)
                        }
                    }
                }
                adapter.data = data
                adapter.notifyDataSetChanged()
            }
        }
    }


    @Deprecated(
        "Deprecated in Java",
        ReplaceWith("picker.onActivityResult(requireActivity(), requestCode, resultCode, data)")
    )
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        picker.onActivityResult(requireActivity(), requestCode, resultCode, data)
    }
}