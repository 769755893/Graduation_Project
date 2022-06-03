package com.app.project.hotel.ui.fragments.home.user.userprofile.passorder

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
import com.app.project.hotel.common.BaseViewModel
import com.example.uitraning.util.Utils
import com.example.uitraning.util.coroutines.Main
import com.example.uitraning.util.log
import com.example.uitraning.util.rx.autoSetupAllFunctions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PassOrderViewModel @Inject constructor(val service: UserApi) : BaseViewModel() {
    val data: MutableLiveData<MutableList<UserOrderListDataModel.Data?>> = MutableLiveData()
    var refreshUI: ((position: Int, state: Int) -> Unit)? = null
    var notifyOrderListEmpty: (() -> Unit)? = null
    var pageCount: MutableLiveData<Int> = MutableLiveData()

    fun getOrderLength(userId: String) {
        service.getOrderLength(userId)
            .autoSetupAllFunctions(1)
            .subscribe({ ans ->
                pageCount.postValue((ans.data as String).toInt())
            }, {}).bindLife()
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
            .subscribe({ ans ->
                if (ans?.data?.size == 0) {
                    Main {
                        notifyOrderListEmpty?.invoke()
                        dialog?.dismiss()
                    }
                } else if (!ans?.data.isNullOrEmpty()) {
                    data.postValue(ans.data!!.toMutableList())
                }
                dialog?.dismiss()
            }, { dialog?.dismiss() }).bindLife()
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
        ).autoSetupAllFunctions(1)
            .subscribe({
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
                dialog.dismiss()
            }, {}).bindLife()
    }
}