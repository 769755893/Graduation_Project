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
import com.app.project.hotel.room.DataBaseManager
import com.app.project.hotel.databinding.FragmentLoginMainBinding
import com.app.project.hotel.room.TmpUser
import com.example.uitraning.util.Utils
import com.example.uitraning.util.coroutines.Co
import com.example.uitraning.util.coroutines.Main
import com.example.uitraning.util.log
import com.example.uitraning.util.rx.Rx
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginMainViewModel @Inject constructor(val service: LoginApi) : ViewModel() {
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
        viewModelScope.launch(Dispatchers.IO) {
            val ans = service.getUserIcon(userid)
            if (ans.msg == "true") {
                userIcon.postValue(Utils.string2Bitmap(ans.data as String)!!.toDrawable(resources))
            }
        }
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
        showProgressDialog: ProgressDialog,
        fragmentManager: FragmentManager
    ) = viewModelScope.launch(Dispatchers.IO) {
        val userName = viewBind.data?.username ?: ""
        val userPass = viewBind.data?.userpass ?: ""
        if (userName.isBlank() || userPass.isBlank()) {
            showProgressDialog.dismiss()
            withContext(Dispatchers.Main) {
                msgCallBack.toast("用户/密码不能为空", false)
            }
        } else {
            var disposable: Disposable? = null
            Rx.interval(1000)
                .take(Long.MAX_VALUE)
                .doOnSubscribe {
                    disposable = it
                }
                .subscribe {
                    if (disposable?.isDisposed == false) {
                        Co.launch {
                            when (service.userLogin(userName, userPass).msg) {
                                "封号中" -> {
                                    disposable?.dispose()
                                    Main {
                                        showProgressDialog.dismiss()
                                        msgCallBack.toast("您的账号已被封禁,请联系管理员", false)
                                    }
                                }
                                "true" -> {
                                    if (disposable?.isDisposed == false) {
                                        Main {
                                            if (disposable?.isDisposed == false) {
                                                disposable?.dispose()
                                                val data = Bundle()
                                                data.putString("userId", userName)
                                                findNavController(fragment).navigate(
                                                    R.id.userMainPage,
                                                    data
                                                )
                                            }
                                            showProgressDialog.dismiss()
                                            msgCallBack.toast("登录成功", true)
                                        }
                                        if (isKeep) {
                                            keepUser(viewBind)
                                        } else {
                                            dao.clearTable()
                                        }
                                    }
                                }
                                "false" -> {
                                    disposable?.dispose()
                                    Main {
                                        showProgressDialog.dismiss()
                                        msgCallBack.toast("用户名或密码错误", false)
                                    }
                                }
                            }
                        }
                    }
                }
        }
    }

    fun forgetPass(fragment: Fragment) {
        findNavController(fragment).navigate(R.id.action_loginMain_to_loginForgetPass)
    }
}