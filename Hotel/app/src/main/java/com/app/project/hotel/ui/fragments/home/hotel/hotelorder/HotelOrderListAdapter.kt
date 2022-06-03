package com.app.project.hotel.ui.fragments.home.hotel.hotelorder

import android.view.View
import androidx.core.text.HtmlCompat
import com.app.project.hotel.R
import com.app.project.hotel.base.responsmodel.HotelOrderListDataModel
import com.app.project.hotel.databinding.ItemHotelOrderBinding
import com.app.project.hotel.common.BaseRecyclerAdapter
import com.example.uitraning.util.Utils
import com.jakewharton.rxbinding3.view.clicks

class HotelOrderListAdapter : BaseRecyclerAdapter<ItemHotelOrderBinding>() {
    var data = mutableListOf<HotelOrderListDataModel.Data?>()
    var callback: HotelOrderListener? = null
    override fun provideLayoutId() = R.layout.item_hotel_order

    override fun getItemCount() = data.size
    override fun initViewCase(binding: ItemHotelOrderBinding, position: Int) {
        binding.data = data[position]
        binding.dayCount =
            data[position]?.startTime?.let { data[position]?.endTime?.let { it1 ->
                Utils.calDateDiffDayLine(it,
                    it1
                )
            } }
        binding.btnCancel.clicks()
            .subscribe {
                callback?.hotelCancel(binding.data?.orderId!!, binding.data?.hotelId!!.toInt(), position)
            }.bindLife()
        binding.btnConfirm.clicks()
            .subscribe {
                callback?.hotelConfirm(binding.data?.orderId!!, position)
            }.bindLife()

        binding.btnDone.clicks()
            .subscribe {
                callback?.orderDone(binding.data?.orderId!!,binding.data?.hotelId!!.toInt(), position)
            }.bindLife()
    }

    override fun initViewCase(
        binding: ItemHotelOrderBinding,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.initViewCase(binding, position, payloads)
        if (payloads[0] == 1) {
            binding.tvOrderStatus.text = HtmlCompat.fromHtml("订单状态: <font color = #ff0000>已确认</font>", HtmlCompat.FROM_HTML_MODE_COMPACT)
            binding.btnConfirm.visibility = View.GONE
            binding.btnCancel.visibility = View.GONE
            binding.btnDone.visibility = View.VISIBLE
        } else if (payloads[0] == 2) {
            binding.tvOrderStatus.text = HtmlCompat.fromHtml("订单状态: <font color = #ff0000>已拒绝</font>", HtmlCompat.FROM_HTML_MODE_COMPACT)
            binding.btnConfirm.visibility = View.GONE
            binding.btnCancel.visibility = View.GONE
            binding.btnDone.visibility = View.GONE
        } else if (payloads[0] == 3) {
            binding.tvOrderStatus.text = HtmlCompat.fromHtml("订单状态: <font color = #ff0000>已完成</font>", HtmlCompat.FROM_HTML_MODE_COMPACT)
            binding.btnConfirm.visibility = View.GONE
            binding.btnCancel.visibility = View.GONE
            binding.btnDone.visibility = View.GONE
        }
    }
}