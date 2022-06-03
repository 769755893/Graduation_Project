package com.app.project.hotel.ui.fragments.login.forget

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.project.hotel.R
import com.app.project.hotel.databinding.FragmentLoginForgetPassBinding
import com.app.project.hotel.base.AppEvent
import com.app.project.hotel.common.BaseFragment
import com.app.project.hotel.common.mwindow
import com.app.project.hotel.ui.view.EditTextDrawableView
import com.example.uitraning.util.Utils
import com.example.uitraning.util.showToast
import com.github.gzuliyujiang.wheelpicker.DatePicker
import com.github.gzuliyujiang.wheelpicker.contract.OnDatePickedListener
import com.github.gzuliyujiang.wheelpicker.entity.DateEntity
import com.github.gzuliyujiang.wheelview.contract.WheelFormatter
import com.jakewharton.rxbinding3.view.clicks
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginForgetPassFragment : BaseFragment<FragmentLoginForgetPassBinding>() {
    private val viewModel by viewModels<LoginForgetViewModel>()
    override fun provideLayoutId() = R.layout.fragment_login_forget_pass
    override fun initCase() {
        observe()
        initView()
    }

    private fun observe() {
        viewModel.data.observe(this) {
            viewBind.data = it
        }

        viewModel.msg.observe(this) {
            if (it is Pair<*, *>) {
                val dialog: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                dialog.apply {
                    this.setTitle("正确")
                    this.setIcon(R.drawable.ic_baseline_check_24)
                    this.setMessage("用户：${it.first} ,您的密码为：${it.second}")
                    this.setPositiveButton("确定", object : DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            p0?.dismiss()
                        }
                    })
                    show()
                }
            } else if (it is AppEvent) {
                when (it) {
                    AppEvent.PASSWORD_ERROR -> {
                        showToast(requireContext(), "密保错误", mwindow, false)
                    }
                    AppEvent.OTHER_ERROR -> {
                        showToast(requireContext(), "未知错误", mwindow, false)
                    }
                    else -> {
                        showToast(requireContext(), "未知错误", mwindow, false)
                    }
                }
            }
        }
    }

    private fun initView() {
        viewBind.root.setOnTouchListener(object : View.OnTouchListener {
            @SuppressLint("ClickableViewAccessibility")
            override fun onTouch(p0: View, p1: MotionEvent): Boolean {
                if (p1.action == MotionEvent.ACTION_DOWN) {
                    Utils.closeKeyboard(p0)
                }
                return false
            }
        })
        viewBind.backBtn.clicks()
            .subscribe {
                findNavController().navigate(R.id.action_loginForgetPass_to_loginMain)
            }.bindLife()

        viewBind.tvBirthDay.drawableClickCallBack = object : EditTextDrawableView.DrawableClick {
            override fun rightDrawableClick() {
                showTimePicker()
            }
        }

        viewBind.btnSearch.clicks()
            .subscribe { viewModel.search(viewBind) }
            .bindLife()

        viewBind.tvBirthDay.apply {
            isCursorVisible = false
            isFocusable = false
        }
    }

    private fun showTimePicker() {
        val picker = DatePicker(requireActivity())
        picker.apply {
            this.setOnDatePickedListener(object : OnDatePickedListener {
                override fun onDatePicked(year: Int, month: Int, day: Int) {
                    viewModel.changedUserData(year.toString() + "年" + month + "月" + day + "日")
                }
            })
            this.bodyView.apply {
                this.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                this.layoutParams.height = 600
            }
            this.wheelLayout.apply {
                this.setCyclicEnabled(true)
                val startDate = DateEntity().apply {
                    this.year = 1999
                    this.month = 3
                    this.day = 21
                }
                val endDate = DateEntity().apply {
                    this.year = 2999
                    this.month = 12
                    this.day = 31
                }
                val defaultDate = DateEntity().apply {
                    this.year = 2022
                    this.month = 3
                    this.day = 30
                }
                this.setRange(startDate, endDate, defaultDate)
                val tesize = 70F
                this.yearWheelView.apply {
                    this.isCurvedEnabled = true
                    this.curvedMaxAngle = 100
                    this.visibleItemCount = 8
                    this.textSize = tesize
                    this.itemSpace = 30
                    this.isIndicatorEnabled = true
                    this.curvedIndicatorSpace = 20
                    this.indicatorColor = Color.GRAY
                    this.selectedTextSize = tesize
                    this.setPadding(0, 0, 0, 0)
                    this.setFormatter(object : WheelFormatter {
                        override fun formatItem(item: Any): String {
                            return item.toString() + "年"
                        }
                    })
                }
                this.monthWheelView.apply {
                    this.isCurvedEnabled = true
                    this.curvedMaxAngle = 100
                    this.visibleItemCount = 8
                    this.textSize = tesize
                    this.itemSpace = 30
                    this.isIndicatorEnabled = true
                    this.curvedIndicatorSpace = 20
                    this.indicatorColor = Color.GRAY
                    this.selectedTextSize = tesize
                    this.setPadding(0, 0, 0, 0)
                    this.setFormatter(object : WheelFormatter {
                        override fun formatItem(item: Any): String {
                            return item.toString() + "月"
                        }
                    })
                }
                this.dayWheelView.apply {
                    this.isCurvedEnabled = true
                    this.curvedMaxAngle = 100
                    this.visibleItemCount = 8
                    this.textSize = tesize
                    this.itemSpace = 30
                    this.isIndicatorEnabled = true
                    this.curvedIndicatorSpace = 20
                    this.indicatorColor = Color.GRAY
                    this.selectedTextSize = tesize
                    this.setPadding(0, 0, 0, 0)
                    this.setFormatter(object : WheelFormatter {
                        override fun formatItem(item: Any): String {
                            return item.toString() + "日"
                        }
                    })
                }
            }
            this.okView.apply {
                this.setTextColor(Color.rgb(0xF5, 0xA6, 0x23))
                this.text = "完了"
            }
            this.cancelView.visibility = View.GONE
            this.show()
        }
    }
}