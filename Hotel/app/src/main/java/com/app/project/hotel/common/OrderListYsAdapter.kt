package com.app.project.hotel.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.project.hotel.R
import com.app.project.hotel.databinding.ItemYsTextBinding

class OrderListYsAdapter: RecyclerView.Adapter<DataBindingViewHolder<ItemYsTextBinding>>() {
    var data = mutableListOf<Int>()
    var pageClick: ((position: Int) -> Unit) ? = null
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DataBindingViewHolder<ItemYsTextBinding> {
        return DataBindingViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_ys_text, parent, false))
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder<ItemYsTextBinding>, position: Int) {
        holder.viewBind.text = data[position].toString()
        holder.viewBind.tvYs.setOnClickListener{
            pageClick?.invoke(position)
        }
    }

    override fun getItemCount() = data.size
}