package com.app.project.hotel.ui.fragments.login.main

import android.app.ProgressDialog
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import com.app.project.hotel.R
import com.app.project.hotel.api.LoginApi
import com.app.project.hotel.base.myapplicationContext
import com.app.project.hotel.common.BaseViewModel
import com.app.project.hotel.common.mwindow
import com.app.project.hotel.room.DataBaseManager
import com.app.project.hotel.databinding.FragmentLoginMainBinding
import com.app.project.hotel.room.TmpUser
import com.example.uitraning.util.Utils
import com.example.uitraning.util.coroutines.Co
import com.example.uitraning.util.coroutines.IO
import com.example.uitraning.util.coroutines.Main
import com.example.uitraning.util.log
import com.example.uitraning.util.rx.Rx
import com.example.uitraning.util.rx.autoSetupAllFunctions
import com.example.uitraning.util.showToast
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

@HiltViewModel
class LoginMainViewModel @Inject constructor(val service: LoginApi) : BaseViewModel() {
    lateinit var msgCallBack: MsgCallBack
    private val dao = DataBaseManager.db.dao
    val loginDataLogin: MutableLiveData<LoginMainData> = MutableLiveData()
    val press: MutableLiveData<Boolean> = MutableLiveData()
    val tmpUser: MutableLiveData<TmpUser> = MutableLiveData()
    val userIcon: MutableLiveData<Drawable> = MutableLiveData()

    init {
        loginDataLogin.value = LoginMainData()
        press.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val user = DataBaseManager.db.dao.queryUser()
            if (user != null) {
                tmpUser.postValue(user!!)
            }
        }
    }

    fun getUserIcon(userid: String, resources: Resources) {
        service.getUserIcon(userid)
            .autoSetupAllFunctions(2)
            .subscribe({ ans ->
                if (ans.msg == "true") {
                    userIcon.postValue(
                        Utils.string2Bitmap(ans.data as String)!!.toDrawable(resources)
                    )
                }
            }, {}).bindLife()
    }

    fun eyeBtnClick(viewBind: FragmentLoginMainBinding) {
        press.value = !press.value!!
        if (press.value == true)
            Utils.setPasswordVisibility(viewBind.etUserPassInput, false)
        else Utils.setPasswordVisibility(viewBind.etUserPassInput, true)
    }

    private fun keepUser(viewBind: FragmentLoginMainBinding) =
        viewModelScope.launch(Dispatchers.IO) {
            val userName = viewBind.etUserNameInput.text.toString()
            val userPass = viewBind.etUserPassInput.text.toString()
            dao.keepUser(
                TmpUser(
                    userName,
                    userPass
                )
            )
        }

    fun loginRequest(
        viewBind: FragmentLoginMainBinding,
        fragment: Fragment,
        isKeep: Boolean,
        showProgressDialog: ProgressDialog
    ) {
        val userName = viewBind.data?.username ?: ""
        val userPass = viewBind.data?.userpass ?: ""
        if (userName.isBlank() || userPass.isBlank()) {
            showProgressDialog.dismiss()
            msgCallBack.toast("用户/密码不能为空", false)
        } else {
            service.userLogin(userName, userPass)
                .autoSetupAllFunctions(2)
                .subscribe({ ans ->
                    when (ans.msg) {
                        "封号中" -> {
                            Main {
                                showProgressDialog.dismiss()
                                msgCallBack.toast("您的账号已被封禁,请联系管理员", false)
                            }
                        }
                        "true" -> {
                            Main {
                                val data = Bundle()
                                data.putString("userId", userName)
                                findNavController(fragment).navigate(
                                    R.id.userMainPage,
                                    data
                                )
                                showProgressDialog.dismiss()
                                msgCallBack.toast("登录成功", true)
                            }
                            if (isKeep) {
                                keepUser(viewBind)
                            } else {
                                Co.launch(Dispatchers.IO) {
                                    dao.clearTable()
                                }
                            }
                        }
                        "false" -> {
                            Main {
                                showProgressDialog.dismiss()
                                msgCallBack.toast("用户名或密码错误", false)
                            }
                        }
                        else -> {}
                    }
                }, {
                    showProgressDialog.dismiss()
                }).bindLife()

        }

    }

    fun forgetPass(fragment: Fragment) {
        findNavController(fragment).navigate(R.id.action_loginMain_to_loginForgetPass)
    }
}