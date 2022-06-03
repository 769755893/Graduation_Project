package com.app.project.hotel.ui.fragments.home.root.supermanageuser

import android.app.ProgressDialog
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.project.hotel.api.SuperApi
import com.app.project.hotel.base.responsmodel.SuperManageUserDataModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SuperManageUserViewModel @Inject constructor(val service: SuperApi) : ViewModel() {
    val data: MutableLiveData<MutableList<SuperManageUserDataModel.Data?>> = MutableLiveData()
    val userLength: MutableLiveData<Int> = MutableLiveData()
    fun flushUserList(
        userId: String? = null,
        userName: String? = null,
        offset: Int? = null,
        dialog: ProgressDialog,
        sortType: Int
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val ans = service.queryUserList(userId, userName, offset, sortType).data
            data.postValue(ans!!.toMutableList())
            dialog.dismiss()
        }
    }

    fun upLoadUserState(userId: String, userState: Int, showUpLoadDialog: ProgressDialog) {
        viewModelScope.launch(Dispatchers.IO) {
            service.upLoadUserState(userId, userState)
            showUpLoadDialog.dismiss()
        }
    }

    fun getUserLength() {
        viewModelScope.launch(Dispatchers.IO) {
            val ans = service.getUserLength()
            userLength.postValue((ans.data as String).toInt())
        }
    }
}