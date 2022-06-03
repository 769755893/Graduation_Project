package com.app.project.hotel.ui.fragments.login.hotel

import android.app.ProgressDialog
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.project.hotel.api.LoginApi
import com.app.project.hotel.api.SuperApi
import com.app.project.hotel.base.myapplicationContext
import com.app.project.hotel.databinding.FragmentLoginHotelBinding
import com.app.project.hotel.common.mwindow
import com.example.uitraning.util.coroutines.Main
import com.example.uitraning.util.showToast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginHotelViewModel @Inject constructor(val service: LoginApi, val root: SuperApi) :
    ViewModel() {
    val data: MutableLiveData<LoginHotelData> = MutableLiveData(LoginHotelData())
    var callback: LoginHotelListener? = null

    fun login(viewBind: FragmentLoginHotelBinding, showProgressDialog: ProgressDialog) =
        viewModelScope.launch(Dispatchers.IO) {
            val uidata = viewBind.data
            val hotelId = uidata?.hotelId
            val hotelCode = uidata?.hotelCode
            if (hotelId.isNullOrEmpty() || hotelCode.isNullOrEmpty()) {
                withContext(Dispatchers.Main) {
                    showProgressDialog.dismiss()
                    showToast(myapplicationContext, "编号/密码不能为空", mwindow, false)
                }
                return@launch
            } else {
                when (service.loginHotel(hotelId.toInt(), hotelCode).msg) {
                    "封号中" -> {
                        Main {
                            showProgressDialog.dismiss()
                            showToast(myapplicationContext, "您的账号已被封禁, 请联系管理员", mwindow, false)
                        }
                    }
                    "true" -> {
                        Main {
                            showProgressDialog.dismiss()
                            showToast(myapplicationContext, "认证成功", mwindow)
                            callback?.loginSuccess(hotelId.toString())
                        }
                    }
                    "false" -> {
                        Main {
                            showProgressDialog.dismiss()
                            showToast(myapplicationContext, "用户或密码错误", mwindow, false)
                        }
                    }
                }
            }
        }

    fun sign(viewBind: FragmentLoginHotelBinding) = viewModelScope.launch(Dispatchers.IO) {
        val uidata = viewBind.data
        val hotelCode = uidata?.hotelCode
        val hotelLocation = uidata?.hotelLocation
        val hotelName = uidata?.hotelName
        val hotelPhone = uidata?.hotelPhone

        if (hotelCode.isNullOrBlank() || hotelLocation.isNullOrBlank() || hotelName.isNullOrBlank()
            || hotelPhone.isNullOrBlank()
        ) {
            withContext(Dispatchers.Main) {
                showToast(myapplicationContext, "请检查必填项,如密码等", mwindow, false)
            }
            return@launch
        } else {
            val ans =
                service.signHotel(hotelName, hotelCode, hotelPhone, hotelLocation).data as String
            Main {
                callback?.showHotelCode(ans, hotelCode)
            }
        }
    }
}