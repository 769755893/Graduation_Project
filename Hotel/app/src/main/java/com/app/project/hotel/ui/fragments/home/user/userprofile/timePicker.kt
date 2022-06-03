package com.app.project.hotel.ui.fragments.home.user.userprofile

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.app.project.hotel.databinding.FragmentUserPassOrderBinding
import com.app.project.hotel.ui.fragments.home.user.userorderlist.UserOrderListAdapter
import com.app.project.hotel.ui.fragments.home.user.userprofile.passorder.UserPassOrderFragment
import com.github.gzuliyujiang.wheelpicker.DatePicker
import com.github.gzuliyujiang.wheelpicker.contract.OnDatePickedListener
import com.github.gzuliyujiang.wheelpicker.entity.DateEntity
import com.github.gzuliyujiang.wheelview.contract.WheelFormatter

@SuppressLint("NotifyDataSetChanged")
@RequiresApi(Build.VERSION_CODES.O)
fun UserPassOrderFragment.showUserPassOrderTimePicker(
    activity: Activity,
    viewBind: FragmentUserPassOrderBinding,
    startTime: Int = 0,
    adapter: UserOrderListAdapter
) {

    val picker = DatePicker(activity)
    picker.apply {
        this.setOnDatePickedListener(object : OnDatePickedListener {
            @SuppressLint("SimpleDateFormat", "NotifyDataSetChanged")
            override fun onDatePicked(year: Int, month: Int, day: Int) {
                adapter.data = viewModel.data.value!!
                if (startTime == 1) {
                    if (month < 10)
                    viewBind.st = year.toString() + "年0" + month + "月" + day + "日"
                    else viewBind.st = year.toString() + "年" + month + "月" + day + "日"
                    val stt = viewBind.st.toString().replace('年','-').replace('月','-').replace('日','-')
                    adapter.flushStarTime(stt)
                }
            }
        })
        this.bodyView.apply {
            this.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            this.layoutParams.height = 600
        }
        this.wheelLayout.apply {
            this.setCyclicEnabled(false)
            val startDate = DateEntity().apply {
                this.year = 2022
                this.month = 1
                this.day = 1
            }
            val endDate = DateEntity().apply {
                this.year = 2023
                this.month = 12
                this.day = 31
            }
            val defaultDate = DateEntity().apply {
                this.year = 2022
                this.month = 4
                this.day = 20
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
                this.setCurvedEnabled(true)
                this.setCurvedMaxAngle(100)
                this.setVisibleItemCount(8)
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
                this.setCurvedEnabled(true)
                this.setCurvedMaxAngle(100)
                this.setVisibleItemCount(8)
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