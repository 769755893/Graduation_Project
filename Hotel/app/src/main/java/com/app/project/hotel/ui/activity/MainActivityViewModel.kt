package com.app.project.hotel.ui.activity

import android.app.AlertDialog
import android.app.ProgressDialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.project.hotel.api.SuperApi
import com.app.project.hotel.common.BaseViewModel
import com.example.uitraning.util.coroutines.Co
import com.example.uitraning.util.coroutines.Main
import com.example.uitraning.util.log
import com.example.uitraning.util.rx.Rx
import com.example.uitraning.util.rx.autoSetupAllFunctions
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(val service: SuperApi) : BaseViewModel() {
    var msgHintCallBack: ((msg: String) -> Unit)? = null

    fun hotelEnterRequest(key: String?, dialog: ProgressDialog) {
        service.hotelEnterRequest(key)
            .autoSetupAllFunctions(1)
            .subscribe({ ans ->
                dialog.dismiss()
                if (ans.msg == "false") {
                    Main {
                        msgHintCallBack?.invoke("hotelfalse")
                    }
                } else {
                    Main {
                        msgHintCallBack?.invoke("hoteltrue")
                    }
                }
            }, {}).bindLife()
    }

    fun superEnterRequest(key: String?, dialog: ProgressDialog) {
        service.superEnterRequest(key)
            .autoSetupAllFunctions(1)
            .subscribe({ ans ->
                dialog.dismiss()
                if (ans.msg == "false") {
                    Main {
                        msgHintCallBack?.invoke("superfalse")
                    }
                } else {
                    Main {
                        msgHintCallBack?.invoke("supertrue")
                    }
                }
            }, {}).bindLife()
    }
}