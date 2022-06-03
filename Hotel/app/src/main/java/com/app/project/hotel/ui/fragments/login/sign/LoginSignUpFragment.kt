package com.app.project.hotel.ui.fragments.login.sign

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.project.hotel.R
import com.app.project.hotel.databinding.FragmentLoginSignUpBinding
import com.app.project.hotel.common.BaseFragment
import com.app.project.hotel.ui.view.EditTextDrawableView
import com.example.uitraning.util.Utils
import com.github.gzuliyujiang.wheelpicker.DatePicker
import com.github.gzuliyujiang.wheelpicker.contract.OnDatePickedListener
import com.github.gzuliyujiang.wheelpicker.entity.DateEntity
import com.github.gzuliyujiang.wheelview.contract.WheelFormatter
import com.jakewharton.rxbinding3.view.clicks
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginSignUpFragment : BaseFragment<FragmentLoginSignUpBinding>() {
    private val viewModel by viewModels<LoginSignViewModel> ()
    override fun provideLayoutId() = R.layout.fragment_login_sign_up

    override fun initCase() {
        viewModel.data.observe(this) {
            viewBind.data = it
        }
        viewBind.root.setOnTouchListener(object : View.OnTouchListener {
            @SuppressLint("ClickableViewAccessibility")
            override fun onTouch(p0: View, p1: MotionEvent): Boolean {
                if (p1.action == MotionEvent.ACTION_DOWN){
                    Utils.closeKeyboard(p0)
                }
                return false
            }
        })
        viewBind.suTvSignBtn.setOnClickListener {
            viewModel.signStart(viewBind)
        }
        viewBind.suTvExitBtn.clicks()
            .subscribe {
                findNavController().navigate(R.id.action_loginSignUp_to_loginMain)
            }.bindLife()
        viewBind.tvBirthDay.isFocusable = false
        viewBind.tvBirthDay.isCursorVisible = false
        viewBind.tvBirthDay.drawableClickCallBack = object : EditTextDrawableView.DrawableClick {
            override fun rightDrawableClick() {
                showTimePicker()
            }
        }
        viewBind.backBtn.clicks()
            .subscribe {
                findNavController().navigate(R.id.action_loginSignUp_to_loginMain)
            }.bindLife()
    }

    private fun showTimePicker() {
        val picker = DatePicker(requireActivity())
        picker.apply {
            this.setOnDatePickedListener(object : OnDatePickedListener {
                override fun onDatePicked(year: Int, month: Int, day: Int) {
                    viewModel.userDateChanged(year.toString() + "年" + month + "月" + day + "日")
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
                this.setRange(startDate,endDate,defaultDate)
                val tesize = 70F
                this.yearWheelView.apply {
                    this.setCurvedEnabled(true)
                    this.setCurvedMaxAngle(100)
                    this.setVisibleItemCount(8)
                    this.textSize = tesize
                    this.itemSpace = 30
                    this.isIndicatorEnabled = true
                    this.curvedIndicatorSpace = 20
                    this.indicatorColor = Color.GRAY
                    this.selectedTextSize = tesize
                    this.setPadding(0,0,0,0)
                    this.setFormatter(object : WheelFormatter {
                        override fun formatItem(item: Any): String {
                            return item.toString() + "年"
                        }
                    })
                }
                this.monthWheelView.apply {
                    this.setCurvedEnabled(true)
                    this.setCurvedMaxAngle(100)
                    this.setVisibleItemCount(8)
                    this.textSize = tesize
                    this.itemSpace = 30
                    this.isIndicatorEnabled = true
                    this.curvedIndicatorSpace = 20
                    this.indicatorColor = Color.GRAY
                    this.selectedTextSize = tesize
                    this.setPadding(0,0,0,0)
                    this.setFormatter(object : WheelFormatter {
                        override fun formatItem(item: Any): String {
                            return item.toString() + "月"
                        }
                    })
                }
                this.dayWheelView.apply {
                    this.setCurvedEnabled(true)
                    this.setCurvedMaxAngle(100)
                    this.setVisibleItemCount(8)
                    this.textSize = tesize
                    this.itemSpace = 30
                    this.isIndicatorEnabled = true
                    this.curvedIndicatorSpace = 20
                    this.indicatorColor = Color.GRAY
                    this.selectedTextSize = tesize
                    this.setPadding(0,0,0,0)
                    this.setFormatter(object : WheelFormatter {
                        override fun formatItem(item: Any): String {
                            return item.toString() + "日"
                        }
                    })
                }
            }
            this.okView.apply {
                this.setTextColor(Color.rgb(0xF5,0xA6,0x23))
                this.text = "确定"
            }
            this.cancelView.visibility = View.GONE
            this.show()
        }
    }


}