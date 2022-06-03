package com.app.project.hotel.util

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

fun <T : Any> RecyclerView.Adapter<*>.diffInvalidate(
    new: List<T>?,
    old: List<T>?
) = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return old?.size ?: 0
    }

    override fun getNewListSize(): Int {
        return new?.size ?: 0
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return true
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return old?.get(oldItemPosition) ?: "" == new?.get(newItemPosition) ?: ""
    }
}).dispatchUpdatesTo(this)