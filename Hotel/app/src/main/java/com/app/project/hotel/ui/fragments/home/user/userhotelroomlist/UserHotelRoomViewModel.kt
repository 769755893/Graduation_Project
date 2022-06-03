package com.app.project.hotel.ui.fragments.home.user.userhotelroomlist

import android.app.ProgressDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.project.hotel.api.UserApi
import com.app.project.hotel.base.responsmodel.HotelCommentDataModel
import com.app.project.hotel.base.responsmodel.UserHotelRoomDataModel
import com.app.project.hotel.ui.fragments.home.user.userhotelroomlist.roomlist.orderdialog.UserOrderData
import com.example.uitraning.util.coroutines.Co
import com.example.uitraning.util.log
import com.example.uitraning.util.rx.Rx
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserHotelRoomViewModel @Inject constructor(val service: UserApi) : ViewModel() {
    var showDialog: (() -> ProgressDialog)? = null
    val data: MutableLiveData<MutableList<UserHotelRoomDataModel.Data?>> = MutableLiveData()
    val hotelCommentData: MutableLiveData<MutableList<HotelCommentDataModel.Data?>> =
        MutableLiveData()
    var CURRENT_HOTEL_ID: String = ""
    var CURRENT_HOTEL_ID_COMMENT: String = ""

    fun initHotelRoomData(hotelId: String) {
        val dialog = showDialog?.invoke()
        if (CURRENT_HOTEL_ID == hotelId) {
            dialog?.dismiss()
            return
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                val ans = service.getHotelRoomList(hotelId.toInt())
                if (ans.data != null) {
                    dialog?.dismiss()
                    ans.data?.forEach {
                        it?.roomName?.log()
                    }
                    data.postValue(ans.data!!.toMutableList())
                }
            }
            CURRENT_HOTEL_ID = hotelId
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun orderPay(data: UserOrderData) {
        viewModelScope.launch(Dispatchers.IO) {
            service.orderPay(
                data.hotelId,
                data.userId,
                data.roomId,
                data.starTime,
                data.endTime,
                data.roomPrice
            )
        }
    }

    fun initHotelCommentData(hotelId: String) {
        if (CURRENT_HOTEL_ID_COMMENT == hotelId) {
            return
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                val ans = service.getHotelCommentList(hotelId.toInt())
                hotelCommentData.postValue(ans.data!!.toMutableList())
            }
            CURRENT_HOTEL_ID_COMMENT = hotelId
        }
    }

    fun refreshCommentData(hotelId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val ans = service.getHotelCommentList(hotelId.toInt())
            hotelCommentData.postValue(ans.data!!.toMutableList())
        }
    }

    fun gooClick(commentId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            service.goodClick(commentId)
        }
    }
}