package com.app.project.hotel.ui.fragments.home.hotel.hotelmanage

import android.annotation.SuppressLint
import androidx.core.graphics.drawable.toDrawable
import com.app.project.hotel.R
import com.app.project.hotel.base.myapplicationContext
import com.app.project.hotel.base.responsmodel.HotelManageOverViewDataModel
import com.app.project.hotel.databinding.ItemHotelManageBinding
import com.app.project.hotel.common.BaseRecyclerAdapter
import com.app.project.hotel.common.DataBindingViewHolder
import com.example.uitraning.util.Utils
import com.example.uitraning.util.log
import com.jakewharton.rxbinding3.view.clicks

class HotelManageAdapter : BaseRecyclerAdapter<ItemHotelManageBinding>() {
    var data = mutableListOf<HotelManageOverViewDataModel.Data?>()
    var upLoadRoomMsg: ((ItemHotelManageBinding, position: Int) -> Unit?)? = null
    var callback: HotelManageListener? = null

    @SuppressLint("NotifyDataSetChanged")
    fun addRoom() {
        data.add(HotelManageOverViewDataModel.Data())
        notifyItemInserted(data.size)
    }

    override fun provideLayoutId(): Int {
        return R.layout.item_hotel_manage
    }

    override fun initViewCase(
        binding: ItemHotelManageBinding,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.initViewCase(binding, position, payloads)
        if (payloads[0] == "iv") {
            binding.ivRoomIcon.setImageDrawable(
                Utils.string2Bitmap(data[position]!!.roomIcon)!!.toDrawable(
                    myapplicationContext.resources
                )
            )
        } else if (payloads[0] == "upLoadChanged") {
            binding.data = binding.data
        }
    }

    override fun initViewCase(binding: ItemHotelManageBinding, position: Int) {
        super.initViewCase(binding, position)
        binding.data = data[position]
        binding.btnUpLoadroomMsg.clicks().subscribe {
            if (binding.etRoomDesc.text.isNullOrEmpty() || binding.etRoomFeature.text.isNullOrEmpty() || binding.etRoomName.text.isNullOrEmpty()
                || binding.etRoomPrice.text.isNullOrEmpty()
            ) {
                callback?.notifyInputMsgWeak()
            } else
                upLoadRoomMsg?.invoke(binding, position)//上传binding信息
        }.bindLife()
        binding.ivRoomIcon.clicks().subscribe {
            callback?.pickImage(binding, position)
        }.bindLife()
    }

    override fun onViewDetachedFromWindow(holder: DataBindingViewHolder<ItemHotelManageBinding>) {
        super.onViewDetachedFromWindow(holder)
        holder.viewBind.data = null
    }

    override fun getItemCount() = data.size
}