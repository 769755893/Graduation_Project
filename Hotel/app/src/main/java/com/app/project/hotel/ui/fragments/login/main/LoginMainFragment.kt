package com.app.project.hotel.ui.fragments.login.main

import android.annotation.SuppressLint
import android.os.Build
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.project.hotel.R
import com.app.project.hotel.base.myapplicationContext
import com.app.project.hotel.databinding.FragmentLoginMainBinding
import com.app.project.hotel.room.DataBaseManager
import com.app.project.hotel.ui.activity.MainActivity
import com.app.project.hotel.ui.activity.showNotUserDialog
import com.app.project.hotel.common.BaseFragment
import com.app.project.hotel.common.mPackageName
import com.app.project.hotel.common.mwindow
import com.app.project.hotel.ui.view.EditTextDrawableView
import com.example.uitraning.util.Utils
import com.example.uitraning.util.showToast
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.textChanges
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess

@AndroidEntryPoint
class LoginMainFragment : BaseFragment<FragmentLoginMainBinding>() {
    private val viewModel by viewModels<LoginMainViewModel>()
    private var keep = false
    private var backPressCnt = 0
    override fun onStart() {
        super.onStart()
        (activity as MainActivity).apply {
            bottomBarVisibility.invoke(View.VISIBLE)
            rightScroll = {
                Toast.makeText(requireContext(), "左边已经到底了哦~", Toast.LENGTH_SHORT).show()
            }
            leftScroll = {
                this.showNotUserDialog("酒店用户")
            }
        }
    }

    override fun onStop() {
        super.onStop()
        (activity as MainActivity).apply {
            bottomBarVisibility.invoke(View.GONE)
            touchClear()
        }
    }

    override fun provideLayoutId() = R.layout.fragment_login_main

    @RequiresApi(Build.VERSION_CODES.N)
    override fun initCase() {
        viewBind.ivSupport.clicks().subscribe {
            findNavController().navigate(R.id.support)
        }.bindLife()

        DataBaseManager.saveApplication(myapplicationContext)
        initView()
        loginStart()
        bindData()
        initEye()
        initCallback()
        initRememberUser()
        textGurad()
    }

    private fun textGurad() {
        viewBind.etUserNameInput.textChanges()
            .throttleFirst(2, TimeUnit.SECONDS)
            .subscribe {
                viewModel.getUserIcon(it.toString(), resources)
            }.bindLife()
        viewBind.etUserPassInput.textChanges()
            .throttleFirst(2, TimeUnit.SECONDS)
            .subscribe {
                viewModel.getUserIcon(viewBind.etUserNameInput.text.toString(), resources)
            }.bindLife()

        viewModel.userIcon.observe(this) {
            if (it != null) {
                val rd = RoundedBitmapDrawableFactory.create(resources, it.toBitmap())
                rd.isCircular = true
                rd.cornerRadius = 250f
                viewBind.ivLoginLogo.setImageDrawable(rd)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewBind.vvBg.start()
    }

    private fun initRememberUser() {
        viewModel.tmpUser.observe(this) {
            viewBind.data?.username = it.userName
            viewBind.data?.userpass = it.userPass
            viewBind.data = viewBind.data
        }
    }

    private fun initCallback() {
        viewModel.msgCallBack = object : MsgCallBack {
            override fun toast(msg: String, state: Boolean) {
                showToast(requireContext(), msg, mwindow, state)
            }
        }
    }

    private fun initEye() {
        viewModel.press.observe(this) {
            viewBind.pressed = it
            if (it == true)
                Utils.setPasswordVisibility(viewBind.etUserPassInput, false)
            else Utils.setPasswordVisibility(viewBind.etUserPassInput, true)
        }
        viewBind.etUserPassInput.drawableClickCallBack =
            object : EditTextDrawableView.DrawableClick {
                override fun rightDrawableClick() {
                    viewModel.eyeBtnClick(viewBind)
                }
            }
    }


    private fun bindData() {
        viewModel.loginDataLogin.observe(this) {
            viewBind.data = it
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {
        viewBind.root.setOnTouchListener { p0, p1 ->
            when (p1.action) {
                MotionEvent.ACTION_DOWN -> {
                    Utils.closeKeyboard(p0)
                }

                MotionEvent.ACTION_UP -> {
                }
            }
            false
        }
        viewBind.vvBg.apply {
            this.setVideoPath("android.resource://" + mPackageName + "/" + R.raw.bg_login)
            this.start()
            this.setOnCompletionListener {
                it.start()
            }
        }
        viewBind.tvForgetPassBtn.setOnClickListener {
            viewModel.forgetPass(this@LoginMainFragment)
        }
        viewBind.keepUserPass.clicks()//checkbox 记住密码
            .subscribe {
                keep = !keep
            }.bindLife()
    }

    private fun loginStart() {
        viewBind.apply {
            tvLoginBtn.clicks()
                .subscribe {
                    viewModel.loginRequest(
                        viewBind,
                        this@LoginMainFragment,
                        keep,
                        showProgressDialog(),
                        requireActivity().supportFragmentManager
                    )
                }.bindLife()
            tvExitBtn.clicks()
                .subscribe {
                    exitProcess(0)
                }.bindLife()
            tvSignUp.clicks()
                .subscribe {
                    findNavController().navigate(R.id.action_loginMain_to_loginSignUp)
                }.bindLife()
        }
    }
}