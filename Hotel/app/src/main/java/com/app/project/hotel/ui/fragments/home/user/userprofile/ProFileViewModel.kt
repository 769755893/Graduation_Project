package com.app.project.hotel.ui.fragments.home.user.userprofile

import android.app.ProgressDialog
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.project.hotel.api.UserApi
import com.app.project.hotel.base.myapplicationContext
import com.app.project.hotel.base.responsmodel.ProFileDataModel
import com.app.project.hotel.common.BaseViewModel
import com.app.project.hotel.databinding.DialogHotelUpdateMsgSuccessBinding
import com.app.project.hotel.ui.fragments.home.user.userhotelroomlist.roomlist.orderdialog.UserOrderData
import com.app.project.hotel.ui.view.dialog.BaseDialog
import com.example.uitraning.util.coroutines.Main
import com.example.uitraning.util.log
import com.example.uitraning.util.rx.autoCatchErrorToast
import com.example.uitraning.util.rx.autoSetupAllFunctions
import com.example.uitraning.util.rx.switchThread
import com.example.uitraning.util.showToast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import javax.inject.Inject

@HiltViewModel
class ProFileViewModel @Inject constructor(val service: UserApi) : BaseViewModel() {
    var firstTime = true
    val data: MutableLiveData<ProFileDataModel.Data?> = MutableLiveData()
    val userMoney: MutableLiveData<String> = MutableLiveData()
    var userChargeState: ((state: Boolean) -> Unit)? = null
    var userPayState: ((state: Boolean) -> Unit)? = null
    var KEEP_USER_ID = ""
    fun initUserData(userId: String) {
        KEEP_USER_ID = userId
        service.getUserMsg(userId)
            .autoSetupAllFunctions(2)
            .subscribe({
                data.postValue(it.data)
            }, {})
            .bindLife()
    }

    fun userPay(money: Int) {
        service.userPay(data.value?.userId!!, money)
            .autoSetupAllFunctions(2)
            .subscribe({
                if (it.msg == "支付成功") {
                    userPayState?.invoke(true)
                } else {
                    userPayState?.invoke(false)
                }
            }, {}).bindLife()
    }

    fun userRecharge(rechargeKey: String) {
        service.userRecharge(data.value?.userId!!, rechargeKey)
            .autoSetupAllFunctions(2)
            .subscribe({
                if (it.msg == "充值失败") {
                    userChargeState?.invoke(false)
                } else {
                    userChargeState?.invoke(true)
                }
            }, {}).bindLife()

    }

    fun getUserMoney() {
        service.getUserMoney(KEEP_USER_ID)
            .autoSetupAllFunctions(2)
            .subscribe({
                userMoney.postValue(it.data as String)
            }, {}).bindLife()
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
        service.upLoadUserMsg(
            bitmapStr,
            data.userName,
            data.userPass,
            data.userLocation,
            data.userPhone,
            data.userId,
            data.userBz
        )
            .autoSetupAllFunctions(2)
            .subscribe({
                dialog.dismiss()
                showUpdateSuccessMsg.show()
            }, {}).bindLife()
    }
}