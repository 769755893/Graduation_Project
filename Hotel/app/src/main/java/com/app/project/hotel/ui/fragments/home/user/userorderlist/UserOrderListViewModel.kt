package com.app.project.hotel.ui.fragments.home.user.userorderlist

import android.annotation.SuppressLint
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
import com.app.project.hotel.base.myapplicationContext
import com.app.project.hotel.base.responsmodel.UserOrderListDataModel
import com.app.project.hotel.common.BaseViewModel
import com.app.project.hotel.common.mwindow
import com.example.uitraning.util.Utils
import com.example.uitraning.util.coroutines.Co
import com.example.uitraning.util.coroutines.Main
import com.example.uitraning.util.log
import com.example.uitraning.util.rx.*
import com.example.uitraning.util.showToast
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Single
import io.reactivex.functions.Consumer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import javax.inject.Inject

@HiltViewModel
class UserOrderListViewModel @Inject constructor(val service: UserApi) : BaseViewModel() {
    val data: MutableLiveData<MutableList<UserOrderListDataModel.Data?>> = MutableLiveData()
    var showAlertMsg: ((position: Int) -> Unit)? = null
    var refreshUI: ((position: Int, state: Int) -> Unit)? = null
    var notifyOrderListEmpty: (() -> Unit)? = null
    var pageCount: MutableLiveData<Int> = MutableLiveData()
    var loadFailed: (() -> Unit)? = null

    fun getOrderLength(userId: String?) {
        if (userId != null)
            service.getOrderLength(userId)
                .switchThread()
                .autoCatchErrorToast()
                .setupTimeOut(1)
                .subscribe({ ans ->
                    pageCount.postValue((ans.data as String).toInt())
                }) { loadFailed?.invoke() }.bindLife()
    }

    fun initOrderListData(
        userId: String? = "",
        hotelId: String = "",
        dialog: ProgressDialog? = null,
        offset: Int? = 0,
        orderTimeType: Int
    ) {
        service.getOrderList(userId, hotelId, offset, orderTimeType)
            .autoSetupAllFunctions(4)
            .subscribe({
                val ans = it.data
                if (ans?.size == 0) {
                    Main {
                        notifyOrderListEmpty?.invoke()
                        dialog?.dismiss()
                    }
                } else {
                    data.postValue(ans!!.toMutableList())
                    dialog?.dismiss()
                }
            }) {
                dialog?.dismiss()
                loadFailed?.invoke()
            }
            .bindLife()
    }

    fun orderCancel(userId: String?, orderId: String?, dialog: ProgressDialog, position: Int) {
        service.cancelOrder(orderId, userId)
            .autoSetupAllFunctions(1)
            .subscribe({
                Main {
                    dialog.dismiss()
                    showAlertMsg?.invoke(position)
                }
            }) { dialog.dismiss() }.bindLife()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun upLoadUserComment(
        data: UserOrderListDataModel.Data,
        context: Context?,
        dialog: ProgressDialog
    ) {
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
            .autoSetupAllFunctions(2)
            .subscribe({
                dialog.dismiss()
                Main {
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
            }) { dialog.dismiss() }.bindLife()
    }

    fun userOrderConfirm(orderId: String) {
        service.userOrderConfirm(orderId)
            .autoSetupAllFunctions(1)
            .subscribe({}, {}).bindLife()
    }
}