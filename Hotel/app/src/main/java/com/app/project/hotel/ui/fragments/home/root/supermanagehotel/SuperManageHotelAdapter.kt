package com.app.project.hotel.ui.fragments.home.root.supermanagehotel

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.project.hotel.R
import com.app.project.hotel.base.responsmodel.SuperManageHotelDataModel
import com.app.project.hotel.databinding.ItemSuperManageHotelParentBinding
import com.app.project.hotel.databinding.ItemSuperManageRoomChildBinding
import com.example.uitraning.util.Utils

class SuperManageHotelAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var data = mutableListOf<SuperManageHotelDataModel.Data?>()
    private val typeParent = 0
    private val typeChild = 1
    var callback: SuperManageHotelListener? = null

    @SuppressLint("NotifyDataSetChanged")
    fun displayChildData(d: MutableList<SuperManageHotelDataModel.Data?>, position: Int) {
        //插入
        data.addAll(position + 1, d)
        notifyItemRangeInserted(position + 1, d.size)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeChildData(position: Int, childSize: Int) {
        data.subList(position + 1, position + 1 + childSize).clear()
        notifyItemRangeRemoved(position + 1, childSize)
        notifyDataSetChanged()
    }

    inner class ParentViewHolder(val binding: ItemSuperManageHotelParentBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class ChildViewHolder(val binding: ItemSuperManageRoomChildBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        if (viewType == typeParent) {
            return ParentViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_super_manage_hotel_parent,
                    parent,
                    false
                )
            )
        } else {
            return ChildViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_super_manage_room_child,
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (data[position]?.parent == true) {
            val myHolder = holder as ParentViewHolder
            val binding = myHolder.binding
            binding.data = data[position]
            initParentViewCase(binding, position)
            holder.itemView.findViewById<TextView>(R.id.tv_hotel_name).isSelected = true
        } else {
            val myHolder = holder as ChildViewHolder
            val binding = myHolder.binding
            binding.data = data[position]
            initChildViewCase(binding, position)
        }
    }
    private fun initChildViewCase(binding: ItemSuperManageRoomChildBinding, position: Int) {
        binding.btnUpLoadroomMsg.setOnClickListener {
            callback?.upLoadRoomStatus(binding.data?.roomId!!, 2 - binding.sr.selectedItemPosition, binding)
        }
    }

    private fun initParentViewCase(binding: ItemSuperManageHotelParentBinding, position: Int) {

        binding.ivArrowDown.setOnClickListener {
            if (binding.data?.foldState == true) {

                binding.data?.foldState = false
                callback?.displayRoomListClick(binding.data?.hotelId!!, position)
            }
            else {

                binding.data?.foldState = true
                removeChildData(position, binding.data?.childSize!!)
            }

        }

        binding.btnSubmit.setOnClickListener {
            callback?.changeHotelState(binding)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (data[position]?.parent == true) {
            return typeParent
        } else return typeChild
    }

    override fun getItemCount() = data.size
    @SuppressLint("NotifyDataSetChanged")
    fun textInputFilter(value: String) {
        if (value.isNotEmpty())
        data = data.filter {
                value in it?.hotelId.toString() ||value in it?.hotelName.toString()
        }.toMutableList()
        notifyDataSetChanged()
    }
}