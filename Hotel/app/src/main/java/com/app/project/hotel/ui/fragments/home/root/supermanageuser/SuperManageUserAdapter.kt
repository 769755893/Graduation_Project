package com.app.project.hotel.ui.fragments.home.root.supermanageuser

import android.annotation.SuppressLint
import android.view.View
import android.widget.AdapterView
import com.app.project.hotel.R
import com.app.project.hotel.base.responsmodel.SuperManageUserDataModel
import com.app.project.hotel.databinding.ItemSuperManageUserBinding
import com.app.project.hotel.common.BaseRecyclerAdapter
import com.jakewharton.rxbinding3.view.clicks

class SuperManageUserAdapter : BaseRecyclerAdapter<ItemSuperManageUserBinding>() {
    var data = mutableListOf<SuperManageUserDataModel.Data?>()
    var btnUpdateCall: ((userId: String, userState: Int) -> Unit)? = null
    override fun provideLayoutId(): Int {
        return R.layout.item_super_manage_user
    }

    override fun getItemCount() = data.size

    override fun initViewCase(binding: ItemSuperManageUserBinding, position: Int) {
        binding.data = data[position]

        binding.btnUpdate.clicks()
            .subscribe {
                btnUpdateCall?.invoke(binding.data?.userId!!, binding.srState.selectedItemPosition)
            }.bindLife()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun textInputFilter(value: String) {
        if (value.isNotEmpty())
            data = data.filter {
                value in it?.userId.toString() || value in it?.userName.toString()
            }.toMutableList()
        notifyDataSetChanged()
    }
}