package com.app.project.hotel.ui.fragments.home.user.userorderlist

import android.annotation.SuppressLint
import android.view.View
import androidx.core.text.HtmlCompat
import com.app.project.hotel.R
import com.app.project.hotel.base.responsmodel.UserOrderListDataModel
import com.app.project.hotel.databinding.ItemUserOrderBinding
import com.app.project.hotel.common.BaseRecyclerAdapter
import com.example.uitraning.util.Utils
import com.example.uitraning.util.Utils.calDateDiffDayLine
import com.example.uitraning.util.log
import com.jakewharton.rxbinding3.view.clicks

class UserOrderListAdapter : BaseRecyclerAdapter<ItemUserOrderBinding>() {
    var data = mutableListOf<UserOrderListDataModel.Data?>()
    var callback: UserOrderListListener? = null
    override fun provideLayoutId() = R.layout.item_user_order

    override fun initViewCase(binding: ItemUserOrderBinding, position: Int) {
        binding.data = data[position]
        binding.dayCount = data[position]?.startTime?.let {
            data[position]?.endTime?.let { it1 ->
                calDateDiffDayLine(
                    it,
                    it1
                )
            }
        }
        binding.btnCancel.clicks()
            .subscribe {
                callback?.orderCancel(binding.data?.userId, binding.data?.orderId, position)
            }.bindLife()

        binding.tvRoomDesc.isSelected = true

        binding.btnUserComment.clicks()
            .subscribe {
                callback?.commentClick(binding.data!!, position)
            }.bindLife()
    }

    override fun initViewCase(
        binding: ItemUserOrderBinding,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.initViewCase(binding, position, payloads)
        if (payloads[0] == "cancel") {
            binding.apply {
                tvOrderStatus.text = HtmlCompat.fromHtml(
                    "订单状态: <font color = #ff0000>用户已取消</font>",
                    HtmlCompat.FROM_HTML_MODE_COMPACT
                )
                btnCancel.visibility = View.GONE
            }
        } else if (payloads[0] == 4) {
            binding.btnUserComment.visibility = View.GONE
        }
    }

    override fun getItemCount() = data.size

    @SuppressLint("NotifyDataSetChanged")
    fun flushStarTime(st: String) {
        data = data.filter {
            val time = it?.orderTime!!.dropLast(9)
            time.log("订单开始时间")
            st.log("st")
            calDateDiffDayLine(time, st) <= 0
        }.toMutableList()
        notifyDataSetChanged()
    }
    @SuppressLint("NotifyDataSetChanged")
    fun flushEndTime(et: String) {
        data = data.filter {
            val time = it?.orderTime!!.dropLast(9)
            time.log("订单结束时间")
            et.log("et")
            calDateDiffDayLine(time,et) >= 0
        }.toMutableList()
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun flushHotelName(text: String) {
        data = data.filter {
            text in it?.hotelName!!
        }.toMutableList()
        notifyDataSetChanged()
    }
}