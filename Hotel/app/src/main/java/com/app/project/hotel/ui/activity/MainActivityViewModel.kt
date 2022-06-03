package com.app.project.hotel.ui.activity

import android.app.AlertDialog
import android.app.ProgressDialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.project.hotel.api.SuperApi
import com.example.uitraning.util.coroutines.Co
import com.example.uitraning.util.coroutines.Main
import com.example.uitraning.util.log
import com.example.uitraning.util.rx.Rx
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(val service: SuperApi) : ViewModel() {
    var msgHintCallBack: ((msg: String) -> Unit)? = null

    fun hotelEnterRequest(key: String?, dialog: ProgressDialog) {
        viewModelScope.launch(Dispatchers.IO) {
            Rx.runInRetryUntilSuccess { disposable ->
                Co.launch {
                    if (disposable?.isDisposed == false) {
                        val ans = service.hotelEnterRequest(key)
                        if (!disposable.isDisposed) {
                            disposable.dispose()
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
                        }
                    }
                }
            }
        }
    }

    fun superEnterRequest(key: String?, dialog: ProgressDialog) {
        viewModelScope.launch(Dispatchers.IO) {
            Rx.runInRetryUntilSuccess { disposable ->
                Co.launch {
                    if (disposable?.isDisposed == false) {
                        val ans = service.superEnterRequest(key)
                        if (!disposable.isDisposed) {
                            disposable.dispose()
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
                        }
                    }
                }
            }
        }
    }
}