package com.app.project.hotel.ui.fragments.home.user.userprofile

import android.app.ProgressDialog
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.project.hotel.api.UserApi
import com.app.project.hotel.base.responsmodel.ProFileDataModel
import com.app.project.hotel.databinding.DialogHotelUpdateMsgSuccessBinding
import com.app.project.hotel.ui.fragments.home.user.userhotelroomlist.roomlist.orderdialog.UserOrderData
import com.app.project.hotel.ui.view.dialog.BaseDialog
import com.example.uitraning.util.log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProFileViewModel @Inject constructor(val service: UserApi) : ViewModel() {
    var firstTime = true
    val data: MutableLiveData<ProFileDataModel.Data?> = MutableLiveData()
    val userMoney: MutableLiveData<String> = MutableLiveData()
    var userChargeState: ((state: Boolean) -> Unit)? = null
    var userPayState: ((state: Boolean) -> Unit)? = null
    fun initUserData(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val ans = service.getUserMsg(userId).data
            data.postValue(ans)
        }
    }

    fun userPay(money: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val ans = service.userPay(data.value?.userId!!, money)
            if (ans.msg == "支付成功") {
                userPayState?.invoke(true)
            } else {
                userPayState?.invoke(false)
            }
        }
    }

    fun userRecharge(rechargeKey: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val ans = service.userRecharge(data.value?.userId!!, rechargeKey)
            if (ans.msg == "充值失败") {
                userChargeState?.invoke(false)
            } else {
                userChargeState?.invoke(true)
            }
        }
    }

    fun getUserMoney() {
        viewModelScope.launch(Dispatchers.IO) {
            val ans = service.getUserMoney(data.value!!.userId)
            userMoney.postValue(ans.data as String)
        }
    }

    fun initOrderData(userOrderData: UserOrderData): UserOrderData {
        userOrderData.apply {
            this.userId = data.value?.userId
            this.userName = data.value?.userName
            this.userPhone = data.value?.userPhone
        }
        return userOrderData
    }

    fun upLoadUserMsg(
        data: ProFileDataModel.Data,
        bitmapStr: String,
        dialog: ProgressDialog,
        showUpdateSuccessMsg: BaseDialog<DialogHotelUpdateMsgSuccessBinding>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            service.upLoadUserMsg(
                bitmapStr,
                data.userName,
                data.userPass,
                data.userLocation,
                data.userPhone,
                data.userId,
                data.userBz
            )
            withContext(Dispatchers.Main) {
                dialog.dismiss()
                showUpdateSuccessMsg.show()
            }
        }
    }
}