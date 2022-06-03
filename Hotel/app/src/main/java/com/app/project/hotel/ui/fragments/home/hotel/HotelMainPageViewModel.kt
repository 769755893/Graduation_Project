package com.app.project.hotel.ui.fragments.home.hotel

import android.app.ProgressDialog
import android.content.Context
import android.content.res.Resources
import android.net.Uri
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.project.hotel.api.HotelApi
import com.app.project.hotel.base.responsmodel.HotelMainPageDataModel
import com.app.project.hotel.base.responsmodel.HotelManageOverViewDataModel
import com.app.project.hotel.databinding.DialogHotelUpdateMsgSuccessBinding
import com.app.project.hotel.databinding.FragmentHotelProfileBinding
import com.app.project.hotel.databinding.ItemHotelManageBinding
import com.app.project.hotel.ui.fragments.home.hotel.hotelmanage.HotelManageAdapter
import com.app.project.hotel.ui.fragments.home.user.userhotelroomlist.roomlist.orderdialog.UserOrderData
import com.app.project.hotel.ui.view.dialog.BaseDialog
import com.example.uitraning.util.Utils
import com.example.uitraning.util.coroutines.Main
import com.example.uitraning.util.log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HotelMainPageViewModel @Inject constructor(val service: HotelApi) : ViewModel() {
    val data: MutableLiveData<HotelMainPageDataModel.Data?> = MutableLiveData()
    val roomListData: MutableLiveData<MutableList<HotelManageOverViewDataModel.Data?>> =
        MutableLiveData()
    var callback: HotelMainPageListener? = null
    var dialogCall: (() -> ProgressDialog)? = null

    fun hotelRoomConfirm(roomId: String) = viewModelScope.launch(Dispatchers.IO) {
        service.hotelRoomConfirm(roomId)
    }

    fun initHotelMsg(hotelId: String) {
        val dialog =  dialogCall?.invoke()
        data.value = null
        viewModelScope.launch(Dispatchers.IO) {
            val ansa = service.getHotelBaseMsg(hotelId.toInt())
            ansa.msg?.log()
            data.postValue(ansa.data!!)

            val ansb = service.getHotelRoomListData(hotelId.toInt())
            dialog?.dismiss()
            roomListData.postValue(ansb.data?.toMutableList()?: mutableListOf(HotelManageOverViewDataModel.Data()))
        }
    }

    fun upLoadRoom(
        it: ItemHotelManageBinding,
        position: Int,
        adapter: HotelManageAdapter,
        dialog: ProgressDialog
    ) {

        viewModelScope.launch(Dispatchers.IO) {
            if (it.data?.roomName == null || it.data?.roomDescription == null || it.data?.roomFeature == null
                || it.data?.roomPrice == null || it.ivRoomIcon.drawable == null
            ) {
                Main {
                    dialog.dismiss()
                    callback?.showDialog("UpLoad Failed, exists row has empty msg~")
                }
            } else {
                var bitmap = it.ivRoomIcon.drawable.toBitmap()
                bitmap = Utils.bitmapResizeFloat(bitmap, 0.7f, 0.7f)
                val ans = service.upLoadRoomMsg(
                    data.value?.hotelId?.toInt()!!,
                    Utils.bitmap2String(bitmap),
                    it.data?.roomName!!,
                    it.data?.roomDescription!!,
                    it.data?.roomFeature!!,
                    it.data?.roomPrice?.toInt()!!,
                    data.value?.hotelId!! + position
                )

                if (ans.msg == "成功") {
                    Main {
                        dialog.dismiss()
                        callback?.showDialog("上传成功")
                        roomListData.value?.get(position)!!.roomUpLoad = true
                        roomListData.value?.get(position)!!.roomPublished = 0
                        adapter.notifyItemChanged(position, "upLoadChanged")
                    }
                } else {
                    Main {
                        callback?.showDialog("UnKnown Error")
                    }
                }
            }
        }
    }

    fun keepPositionImage(
        position: Int,
        imageUri: Uri?,
        context: Context,
        hotelManageAdapter: HotelManageAdapter
    ) {
        var bitmap = Utils.getBitmapFromUri(
            imageUri!!,
            context
        )

        bitmap = Utils.bitmapResizeFloat(bitmap!!, 0.7f, 0.7f)

        val value = roomListData.value
        value?.get(position)?.roomIcon = Utils.bitmap2String(bitmap)
        value?.get(position)?.roomId = data.value?.hotelId + position
        hotelManageAdapter.data = value!!
        hotelManageAdapter.notifyItemChanged(position, "iv")
        roomListData.value = value!!
    }

    fun upLoadHotelIcon(
        viewBind: FragmentHotelProfileBinding,
        dialog: ProgressDialog,
        sd: BaseDialog<DialogHotelUpdateMsgSuccessBinding>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val bitMapstr = Utils.bitmap2String(viewBind.ivHotelIcon.drawable.toBitmap())
            service.upLoadHotelIconMsg(
                bitMapstr,
                viewBind.data?.hotelId!!.toInt(),
                viewBind.data?.hotelDesc!!,
                viewBind.data?.hotelMinPrice!!
            )
            dialog.dismiss()
            withContext(Dispatchers.Main) {
                sd.show()
            }
        }
    }
}