package com.app.project.hotel.ui.view.pickerview

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.app.project.hotel.databinding.FragmentUserRoomListBinding
import com.example.uitraning.util.Utils
import com.example.uitraning.util.log
import com.github.gzuliyujiang.wheelpicker.DatePicker
import com.github.gzuliyujiang.wheelpicker.contract.OnDatePickedListener
import com.github.gzuliyujiang.wheelpicker.entity.DateEntity
import com.github.gzuliyujiang.wheelview.contract.WheelFormatter
import java.text.SimpleDateFormat
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
fun showTimePicker(activity: Activity, viewBind: FragmentUserRoomListBinding, startTime: Int = 0) {
    val picker = DatePicker(activity)
    picker.apply {
        this.setOnDatePickedListener(object : OnDatePickedListener {
            @SuppressLint("SimpleDateFormat")
            override fun onDatePicked(year: Int, month: Int, day: Int) {
                if (startTime == 1)
                viewBind.startTime = year.toString() + "年" + month + "月" + day + "日"
                else {
                    viewBind.endTime = year.toString() + "年" + month + "月" + day + "日"
                    val sdf = SimpleDateFormat("yyyy年MM月dd日")
                    val st = sdf.parse(viewBind.startTime)!!
                    val et = sdf.parse(viewBind.endTime)!!
                    val daycount = Utils.CalDateDiff(st,et)
                    viewBind.dayCount = daycount
                }
            }
        })
        this.bodyView.apply {
            this.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            this.layoutParams.height = 600
        }
        this.wheelLayout.apply {
            this.setCyclicEnabled(false)
            val t = Utils.getNowTime()
            val startDate = DateEntity().apply {
                if (startTime == 0) {
                    val st = viewBind.startTime?.split('年','月','日')
                    this.year = st?.get(0)?.toInt() ?: 2022
                    this.month = st?.get(1)?.toInt() ?: 4
                    this.day = st?.get(2)?.toInt() ?: 20
                } else {
                    this.year = t.first
                    this.month = t.second
                    this.day = t.third
                }
            }
            val endDate = DateEntity().apply {
                this.year = t.first + 1
                this.month = t.second
                this.day = t.third
            }
            val defaultDate = DateEntity().apply {
                this.year = t.first
                this.month = t.second
                this.day = t.third
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