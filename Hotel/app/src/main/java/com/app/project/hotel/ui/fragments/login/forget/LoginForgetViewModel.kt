package com.app.project.hotel.ui.fragments.login.forget

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.project.hotel.api.LoginApi
import com.app.project.hotel.base.myapplicationContext
import com.app.project.hotel.databinding.FragmentLoginForgetPassBinding
import com.app.project.hotel.base.AppEvent
import com.example.uitraning.util.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginForgetViewModel @Inject constructor(val service: LoginApi) : ViewModel() {
    val data: MutableLiveData<ForgetPassData> = MutableLiveData(ForgetPassData())
    val msg: MutableLiveData<Any> = MutableLiveData()

    fun search(viewBind: FragmentLoginForgetPassBinding) = viewModelScope.launch(Dispatchers.IO) {
        val userName = viewBind.data?.userName
        val userDate = viewBind.data?.userDate
        if (userName?.isEmpty() == true || userDate?.isEmpty() == true) {
            Utils.toast(myapplicationContext, "用户名/密保不能为空", false)
        } else {
            val ans = service.forgetQuery(userName, userDate)
            when (ans.msg) {
                "正确" -> {
                    msg.postValue(Pair(userName, ans.data as String))
                }
                "密保错误" -> {
                    msg.postValue(AppEvent.PASSWORD_ERROR)
                }
            }
        }
    }

    fun changedUserData(s: String) {
        data.value?.userDate = s
        data.postValue(data.value)
    }
}