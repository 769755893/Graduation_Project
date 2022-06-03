package com.app.project.hotel.ui.fragments.home.user.userorderlist

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.project.hotel.R
import com.app.project.hotel.api.UserApi
import com.app.project.hotel.base.responsmodel.UserOrderListDataModel
import com.example.uitraning.util.Utils
import com.example.uitraning.util.coroutines.Main
import com.example.uitraning.util.log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserOrderListViewModel @Inject constructor(val service: UserApi) : ViewModel() {
    val data: MutableLiveData<MutableList<UserOrderListDataModel.Data?>> = MutableLiveData()
    var showAlertMsg: ((position: Int) -> Unit)? = null
    var refreshUI: ((position: Int, state: Int) -> Unit)? = null
    var notifyOrderListEmpty: (() -> Unit)? = null
    var pageCount: MutableLiveData<Int> = MutableLiveData()

    fun getOrderLength(userId: String) = viewModelScope.launch(Dispatchers.IO)
    {
        val ans = service.getOrderLength(userId)
        pageCount.postValue((ans.data as String).toInt())
    }

    fun initOrderListData(
        userId: String? = "",
        hotelId: String = "",
        dialog: ProgressDialog? = null,
        offset: Int? = 0,
        orderTimeType: Int
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val ans = service.getOrderList(userId, hotelId, offset, orderTimeType).data
            if (ans?.size == 0) {
                Main {
                    notifyOrderListEmpty?.invoke()
                    dialog?.dismiss()
                }
            } else {
                data.postValue(ans!!.toMutableList())
                dialog?.dismiss()
            }
        }
    }

    fun orderCancel(userId: String?, orderId: String?, dialog: ProgressDialog, position: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            service.cancelOrder(orderId, userId)
            dialog.dismiss()
            withContext(Dispatchers.Main) {
                showAlertMsg?.invoke(position)
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun upLoadUserComment(
        data: UserOrderListDataModel.Data,
        context: Context?,
        dialog: ProgressDialog
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val commentId = data.userId + data.roomId + Utils.getNowTimeString()
            service.upLoadUserComment(
                data.orderId!!,
                commentId,
                data.hotelId!!.toInt(),
                data.userId!!,
                data.userComment!!,
                data.userCommentScore!!,
                data.startTime!!,
                data.roomId!!
            )
            dialog.dismiss()
            withContext(Dispatchers.Main) {
                AlertDialog.Builder(context)
                    .setIcon(R.drawable.ic_baseline_check_24)
                    .setMessage("提交成功")
                    .setPositiveButton("Confirm", object : DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            p0?.dismiss()
                        }
                    })
                    .show()
            }
        }
    }

    fun userOrderConfirm(orderId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            orderId.log("确认的OrderId")
            service.userOrderConfirm(orderId)
        }
    }
}