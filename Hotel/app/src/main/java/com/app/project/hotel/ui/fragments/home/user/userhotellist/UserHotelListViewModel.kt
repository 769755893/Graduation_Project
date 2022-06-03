package com.app.project.hotel.ui.fragments.home.user.userhotellist

import android.app.ProgressDialog
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.project.hotel.api.UserApi
import com.app.project.hotel.base.responsmodel.HotelListDataModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserHotelListViewModel @Inject constructor(val service: UserApi) : ViewModel() {
    var data: MutableLiveData<MutableList<HotelListDataModel.Data?>> = MutableLiveData()
    var roomPageData: MutableLiveData<HotelListDataModel.Data?> = MutableLiveData()
    var cancelFilter: (() -> Unit)? = null
    var filter:((text: String) -> Unit) ? =null
    var pageCount: MutableLiveData<Int> = MutableLiveData()

    fun getHotelCount() {
        viewModelScope.launch(Dispatchers.IO) {
            val ans = service.getHotelCount()
            pageCount.postValue((ans.data as String).toInt())
        }
    }

    fun initData(offset: Int ?= 0, sortType: Int, keyWord: String ? = "", showProgressDialog: ProgressDialog) {
        viewModelScope.launch(Dispatchers.IO){
            val ans = service.getHotelList(sortType, offset!!, keyWord).data
            if (ans?.size != 0)
                data.postValue(ans!!.toMutableList())
            showProgressDialog.dismiss()
        }
    }

    fun initRoomPageIconAndName(hotelId: String) {
        data.value?.forEach {
            if (it?.hotelId == hotelId) {
                roomPageData.postValue(it)
            }
        }
    }
}