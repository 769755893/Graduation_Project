package com.app.project.hotel.ui.fragments.home.user.userhotelroomlist

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.project.hotel.api.UserApi
import com.app.project.hotel.base.myapplicationContext
import com.app.project.hotel.base.responsmodel.HotelCommentDataModel
import com.app.project.hotel.base.responsmodel.UserHotelRoomDataModel
import com.app.project.hotel.common.BaseViewModel
import com.app.project.hotel.ui.fragments.home.user.userhotelroomlist.roomlist.orderdialog.UserOrderData
import com.example.uitraning.util.coroutines.Co
import com.example.uitraning.util.coroutines.Main
import com.example.uitraning.util.log
import com.example.uitraning.util.rx.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserHotelRoomViewModel @Inject constructor(val service: UserApi) : BaseViewModel() {
    val data: MutableLiveData<MutableList<UserHotelRoomDataModel.Data?>> = MutableLiveData()
    val hotelCommentData: MutableLiveData<MutableList<HotelCommentDataModel.Data?>> =
        MutableLiveData()
    var KEPP_HOTEL_ID_TEMPLY: String = ""
    var CURRENT_HOTEL_ID: String = ""
    var CURRENT_HOTEL_ID_COMMENT: String = ""

    fun initHotelRoomData(hotelId: String, dialog: ProgressDialog) {
        dialog.show()
        KEPP_HOTEL_ID_TEMPLY = hotelId
        if (CURRENT_HOTEL_ID == hotelId) {
            dialog.dismiss()
            return
        } else {
            service.getHotelRoomList(hotelId.toInt())
                .autoSetupAllFunctions(8)
                .subscribe({ ans ->
                    data.postValue(ans.data?.toMutableList() ?: mutableListOf())
                    dialog.dismiss()
                    CURRENT_HOTEL_ID = hotelId
                }, {
                    dialog.dismiss()
                }).bindLife()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun orderPay(data: UserOrderData) {
        service.orderPay(
            data.hotelId,
            data.userId,
            data.roomId,
            data.starTime,
            data.endTime,
            data.roomPrice
        )
            .switchThread()
            .autoCatchErrorToast()
            .setupTimeOut(2)
            .subscribe({}, {}).bindLife()
    }

    fun initHotelCommentData(hotelId: String) {
        if (CURRENT_HOTEL_ID_COMMENT == hotelId) {
            return
        } else {
            service.getHotelCommentList(hotelId.toInt())
                .switchThread()
                .autoCatchErrorToast()
                .setupTimeOut(3)
                .subscribe({ ans ->
                    hotelCommentData.postValue(ans.data!!.toMutableList())
                    CURRENT_HOTEL_ID_COMMENT = hotelId
                }, {}).bindLife()
        }
    }

    fun refreshCommentData(hotelId: String, dialog: ProgressDialog ? = null) {
        service.getHotelCommentList(hotelId.toInt())
            .switchThread()
            .autoCatchErrorToast()
            .setupTimeOut(3)
            .subscribe({ ans ->
                dialog?.dismiss()
                hotelCommentData.postValue(ans.data!!.toMutableList())
            }, {
                dialog?.dismiss()
            }).bindLife()
    }

    fun gooClick(commentId: String) {
        service.goodClick(commentId)
            .switchThread()
            .autoCatchErrorToast()
            .setupTimeOut(1)
            .subscribe({}, {}).bindLife()
    }
}