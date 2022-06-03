package com.app.project.hotel.ui.fragments.home.hotel.hotelorder

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.project.hotel.R
import com.app.project.hotel.api.HotelApi
import com.app.project.hotel.base.responsmodel.HotelOrderListDataModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HotelOrderViewModel @Inject constructor(val service: HotelApi): ViewModel() {
    val data: MutableLiveData<MutableList<HotelOrderListDataModel.Data?>> = MutableLiveData()
    val orderLength: MutableLiveData<Int> = MutableLiveData()
    var notifyOrderListEmpty: (() -> Unit)?=null
    var refreshUI: ((position: Int, state: Int) -> Unit)? = null

    fun hotelConfirm(orderId: String, context: Context?, dialog: ProgressDialog, position: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            service.hotelConfirm(orderId)
            dialog.dismiss()
            withContext(Dispatchers.Main) {
                AlertDialog.Builder(context)
                    .setIcon(R.drawable.ic_baseline_check_24)
                    .setMessage("确认成功")
                    .setPositiveButton("Confirm", object : DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            p0?.dismiss()
                            refreshUI?.invoke(position, 1)
                        }
                    }).show()
            }
        }
    }

    fun hotelCancel(orderId: String, hotelId: Int, reason: String?,  context: Context?, dialog: ProgressDialog, position: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            service.hotelReject(orderId, hotelId, reason)
            dialog.dismiss()
            withContext(Dispatchers.Main) {
                AlertDialog.Builder(context)
                    .setIcon(R.drawable.ic_baseline_check_24)
                    .setMessage("拒绝成功")
                    .setPositiveButton("Confirm", object : DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            p0?.dismiss()
                            refreshUI?.invoke(position, 2)
                        }
                    })
                    .show()
            }
        }
    }

    fun hotelOrderDone(orderId: String, hotelId: Int, context: Context?, dialog: ProgressDialog, position: Int) {
        viewModelScope.launch (Dispatchers.IO){
            service.hotelOrderDone(orderId, hotelId)
            dialog.dismiss()
            withContext(Dispatchers.Main) {
                AlertDialog.Builder(context)
                    .setIcon(R.drawable.ic_baseline_check_24)
                    .setMessage("订单已完成")
                    .setPositiveButton("Confirm", object : DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            p0?.dismiss()
                            refreshUI?.invoke(position, 3)
                        }
                    })
                    .show()
            }
        }
    }

    fun initOrderListData(hotelId: String?, dialog: ProgressDialog, offset: Int? = 0) {
        viewModelScope.launch (Dispatchers.IO){
            val ans = service.getHotelOrderList(hotelId!!.toInt(), offset)
            if (ans.data?.size != 0) {
                data.postValue(ans.data!!.toMutableList())
            }
            dialog.dismiss()
        }
    }

    fun getOrderLength(hotelId: Int) {
        viewModelScope.launch (Dispatchers.IO){
            val ans = service.getOrderLength(hotelId)
            orderLength.postValue((ans.data as String).toInt())
        }
    }
}