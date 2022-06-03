package com.app.project.hotel.ui.fragments.login.sign

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.project.hotel.api.LoginApi
import com.app.project.hotel.base.myapplicationContext
import com.app.project.hotel.common.BaseViewModel
import com.app.project.hotel.databinding.FragmentLoginSignUpBinding
import com.app.project.hotel.common.mwindow
import com.example.uitraning.util.Utils
import com.example.uitraning.util.coroutines.Main
import com.example.uitraning.util.rx.autoSetupAllFunctions
import com.example.uitraning.util.showToast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginSignViewModel @Inject constructor(val service: LoginApi) : BaseViewModel() {
    val data: MutableLiveData<SignData> = MutableLiveData(SignData())
    fun signStart(viewBind: FragmentLoginSignUpBinding) {
        val userName = viewBind.data?.userName ?: ""
        val userPass = viewBind.data?.userPass ?: ""
        val userDate = viewBind.data?.userDate ?: ""
        if (userName.isEmpty() || userPass.isEmpty() || userDate.isEmpty()) {
            showToast(myapplicationContext, "用户名 or 密码为空~", mwindow, false)
            return
        } else {
            service.userSign(userName, userPass, userDate)
                .autoSetupAllFunctions(1)
                .subscribe({ ans ->
                    when (ans.msg) {
                        "用户已存在" -> {
                            Main {
                                showToast(myapplicationContext, "用户已存在", mwindow, false)
                            }
                        }
                        "成功" -> {
                            Main {
                                showToast(myapplicationContext, "注册成功", mwindow, true)
                            }
                        }
                    }
                }, {}).bindLife()
        }
    }

    fun userDateChanged(s: String) {
        data.value?.userDate = s
        data.postValue(data.value)
    }
}