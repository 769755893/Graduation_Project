package com.app.project.hotel.ui.activity

import android.app.AlertDialog
import android.view.MotionEvent
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.findNavController
import com.app.project.hotel.R
import com.app.project.hotel.common.BaseMainActivity
import com.example.uitraning.util.Utils
import com.example.uitraning.util.log
import com.jakewharton.rxbinding3.view.clicks
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseMainActivity() {
    val viewModel by viewModels<MainActivityViewModel>()

    val bottomBarVisibility: ((value: Int) -> Unit) = {
        viewBind?.navigationBottomBar?.visibility = it
    }
    private var dx = 0f
    private var ux = 0f
    var rightScroll: (() -> Unit)? = null
    var leftScroll: (() -> Unit)? = null
    fun touchClear() {
        rightScroll = null
        leftScroll = null
    }

    var touchY: ((value: Float) -> Unit)? = null
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_MOVE -> {
                touchY?.invoke(event.y)
            }
            MotionEvent.ACTION_DOWN -> {
                dx = event.x
            }
            MotionEvent.ACTION_UP -> {
                ux = event.x
                if (ux > dx) {
                    if (ux - dx > 400)
                        rightScroll?.invoke()
                } else if (ux < dx) {
                    if (dx - ux > 400)
                        leftScroll?.invoke()
                }
            }
        }
        return true
    }

    override fun initCase() {
        Utils.fullScreen(window)
        Utils.checkPermission(applicationContext, this)
        initViewCaseFirst()

        viewModel.msgHintCallBack = {
            if ("true" in it) {
                "CallBackReturn".log(it)
                viewBind?.navigationBottomBar?.visibility = View.GONE
                if ("hotel" in it) {
                    findNavController(R.id.nav_register_frag).navigate(R.id.action_loginMain_to_login_Hotel)
                } else {
                    findNavController(R.id.nav_register_frag).navigate(R.id.loginSuperUser)
                }
            } else {
                "CallBackReturn".log(it)
                AlertDialog.Builder(this)
                    .setIcon(R.drawable.ic_baseline_error_24)
                    .setTitle("提示")
                    .setMessage("Key 有误")
                    .setPositiveButton(
                        "Confirm"
                    ) { p0, _ -> p0?.dismiss() }
                    .show()
            }
        }
    }

    fun addBackPressCallBack(lifecycleOwner: LifecycleOwner, onBackPressedCallback: OnBackPressedCallback) {
        onBackPressedDispatcher.addCallback(lifecycleOwner, onBackPressedCallback)
    }

    private var currentIndex = 0
    var changeIndex: ((index: Int) -> Unit) = {
        currentIndex = it
    }

    private fun initViewCaseFirst() {
        viewBind.apply {
            this?.btnUser?.clicks()?.subscribe {
                if (currentIndex != 0) {
                    findNavController(R.id.nav_register_frag).navigate(R.id.loginMain)
                    currentIndex = 0
                }
            }?.bindLife()
            this?.btnHotel?.clicks()?.subscribe {
                showNotUserDialog("酒店用户")
            }?.bindLife()
            this?.btnSuper?.clicks()?.subscribe {
                showNotUserDialog("超级用户")
            }?.bindLife()
        }
    }
}