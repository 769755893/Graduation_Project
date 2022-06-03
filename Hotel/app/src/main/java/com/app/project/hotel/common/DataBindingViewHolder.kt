package com.app.project.hotel.common

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView


class DataBindingViewHolder<T: ViewDataBinding>(val viewBind: T): RecyclerView.ViewHolder(viewBind.root)