package com.app.project.hotel.ui.fragments.home.root.supermanagehotel

import android.app.ProgressDialog
import androidx.core.text.HtmlCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.project.hotel.api.SuperApi
import com.app.project.hotel.base.responsmodel.SuperManageHotelDataModel
import com.app.project.hotel.databinding.ItemSuperManageHotelParentBinding
import com.app.project.hotel.databinding.ItemSuperManageRoomChildBinding
import com.example.uitraning.util.coroutines.Main
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SuperManageHotelViewModel @Inject constructor(val service: SuperApi) : ViewModel() {
    val parentData: MutableLiveData<MutableList<SuperManageHotelDataModel.Data?>> =
        MutableLiveData()
    val childData: MutableLiveData<MutableList<SuperManageHotelDataModel.Data?>> = MutableLiveData()
    val hotelLength: MutableLiveData<Int> = MutableLiveData()
    fun downLoadHotelList(
        hotelId: Int? = null,
        hotelName: String? = null,
        hotelLocation: String? = null,
        offset: Int? = null,
        dialog: ProgressDialog,
        sortType: Int
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val ans = service.queryHotelList(hotelId, hotelName, hotelLocation, offset, sortType)
            if (ans.data?.size != 0) {
                parentData.postValue(ans.data!!.toMutableList())
            }
            dialog.dismiss()
        }
    }

    fun downLoadRoomList(hotelId: String, downRoomDialog: ProgressDialog) {
        viewModelScope.launch(Dispatchers.IO) {
            val ans = service.queryHotelRoomList(hotelId.toInt())
            if (ans.data?.size == 0) {
                downRoomDialog.dismiss()
            } else {
                childData.postValue(ans.data!!.toMutableList())
                parentData.value?.forEach {
                    if (it?.hotelId == hotelId) {
                        it.childSize = ans.data!![0]?.childSize
                    }
                }
            }
        }
    }

    fun upLoadRoomStatus(
        roomId: String,
        publishStatus: Int,
        upLoadStatusDialog: ProgressDialog,
        binging: ItemSuperManageRoomChildBinding,
        reason: String?
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            service.changedRoomStatus(roomId, publishStatus, reason)
            upLoadStatusDialog.dismiss()
            Main {
                binging.tvRoomCheckStatus.text = "已审核"
            }
        }
    }

    fun changeHotelState(
        hotelId: String?,
        state: Int,
        showUpLoadStatusProgress: ProgressDialog,
        binding: ItemSuperManageHotelParentBinding
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            service.changeHotelState(hotelId, state)
            showUpLoadStatusProgress.dismiss()
            Main {
                if (state == 0)
                    binding.tvHotelState.text = HtmlCompat.fromHtml(
                        "用户状态: <font color = #ff0000>正常</font>",
                        HtmlCompat.FROM_HTML_MODE_COMPACT
                    )
                else
                    binding.tvHotelState.text = HtmlCompat.fromHtml(
                        "用户状态: <font color = #ff0000>封禁</font>",
                        HtmlCompat.FROM_HTML_MODE_COMPACT
                    )
            }
        }
    }

    fun getHotelLength() {
        viewModelScope.launch(Dispatchers.IO) {
            val ans = service.getHotelLength()
            hotelLength.postValue((ans.data as String).toInt())
        }
    }
}