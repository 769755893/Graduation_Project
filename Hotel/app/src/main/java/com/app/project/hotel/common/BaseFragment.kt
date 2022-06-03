package com.app.project.hotel.common

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.example.uitraning.util.Utils
import com.example.uitraning.util.coroutines.Co
import com.example.uitraning.util.log
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

abstract class BaseFragment<T : ViewDataBinding> : Fragment(), BindLife {
    lateinit var viewBind: T
    override val compositeDisposable: CompositeDisposable
        get() = CompositeDisposable()
    /*Forbidden the every time call back init affect the viewModel data init twice.
    */
    var DENY_FIRST_TIME_CALL_BACK_INIT = true
    /*
    Control the ViewModel Data Just Init Once.
    */
    var IS_FIRST_TIME_VIEWMODEL_GET_DATA = true

    @LayoutRes
    abstract fun provideLayoutId(): Int
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewBind = DataBindingUtil.inflate(inflater, provideLayoutId(), container, false)
        viewBind.lifecycleOwner = activity

        viewBind.root.setOnTouchListener(object : View.OnTouchListener {
            @SuppressLint("ClickableViewAccessibility")
            override fun onTouch(p0: View, p1: MotionEvent): Boolean {
                if (p1.action == MotionEvent.ACTION_DOWN) {
                    Utils.closeKeyboard(p0)
                }
                return false
            }
        })
        return viewBind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCase()
        initCaseSecond()
        initCaseThird()
        /*
        Delay Time is to let the Call Back invoke ViewModel interface come to back work until the every time view recreate done.
        */
        Co.launch {
            delay(1000)
            DENY_FIRST_TIME_CALL_BACK_INIT = false
        }
        if (IS_FIRST_TIME_VIEWMODEL_GET_DATA) {
            IS_FIRST_TIME_VIEWMODEL_GET_DATA = false
            initViewModelData()
        }
    }

    open fun initViewModelData() {}

    open fun initCaseThird() {}

    open fun initCaseSecond() {}

    abstract fun initCase()

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        /*
        Restore the State for the next time View recreate
         */
        DENY_FIRST_TIME_CALL_BACK_INIT = true
        compositeDisposable.clear()
    }

    override fun onDetach() {
        super.onDetach()
        compositeDisposable.clear()
    }
}