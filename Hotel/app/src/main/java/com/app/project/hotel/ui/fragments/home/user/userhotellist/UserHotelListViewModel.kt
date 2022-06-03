package com.app.project.hotel.ui.fragments.home.user.userhotellist

import android.app.ProgressDialog
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.project.hotel.api.UserApi
import com.app.project.hotel.base.responsmodel.HotelListDataModel
import com.app.project.hotel.common.BaseViewModel
import com.app.project.hotel.common.LoginState
import com.example.uitraning.util.rx.autoCatchErrorToast
import com.example.uitraning.util.rx.autoSetupAllFunctions
import com.example.uitraning.util.rx.setupTimeOut
import com.example.uitraning.util.rx.switchThread
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class UserHotelListViewModel @Inject constructor(val service: UserApi) : BaseViewModel() {
    var data: MutableLiveData<MutableList<HotelListDataModel.Data?>> = MutableLiveData()
    var roomPageData: MutableLiveData<HotelListDataModel.Data?> = MutableLiveData()
    var cancelFilter: (() -> Unit)? = null
    var filter: ((text: String) -> Unit)? = null
    var pageCount: MutableLiveData<Int> = MutableLiveData()
    var loadFailed: (() -> Unit)? = null

    fun getHotelCount() {
        service.getHotelCount()
            .switchThread()
            .autoCatchErrorToast()
            .setupTimeOut(2)
            .subscribe({
                pageCount.postValue((it.data as String).toInt())
            }, {}).bindLife()
    }

    fun getHotelList(
        offset: Int? = 0,
        sortType: Int,
        keyWord: String? = "",
        showProgressDialog: ProgressDialog
    ) {
        service.getHotelList(sortType, offset!!, keyWord)
            .autoSetupAllFunctions(4)
            .subscribe({
                if (it?.data?.size != 0)
                    data.postValue(it!!.data?.toMutableList())
                showProgressDialog.dismiss()
            }, {
                showProgressDialog.dismiss()
                loadFailed?.invoke()
            }).bindLife()
    }

    fun initRoomPageIconAndName(hotelId: String) {
        data.value?.forEach {
            if (it?.hotelId == hotelId) {
                roomPageData.postValue(it)
            }
        }
    }
}