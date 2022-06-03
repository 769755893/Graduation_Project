package com.app.project.hotel.common

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.uitraning.util.Utils
import io.reactivex.disposables.CompositeDisposable

abstract class BaseRecyclerAdapter<T : ViewDataBinding> : RecyclerView.Adapter<DataBindingViewHolder<T>>(),
    BindLife {

    override val compositeDisposable: CompositeDisposable
        get() = CompositeDisposable()

    @LayoutRes
    abstract fun provideLayoutId(): Int

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DataBindingViewHolder<T> {
        val view = DataBindingUtil.inflate<T>(
            LayoutInflater.from(parent.context), provideLayoutId(), parent, false
        )
        view.root.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View, p1: MotionEvent): Boolean {
                if (p1.action == MotionEvent.ACTION_DOWN)
                    Utils.closeKeyboard(p0)
                return false
            }
        })

        return DataBindingViewHolder(view)
    }

    final override fun onBindViewHolder(holder: DataBindingViewHolder<T>, position: Int) {

    }

    final override fun onBindViewHolder(
        holder: DataBindingViewHolder<T>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
        if (payloads.size == 0) {
            initViewCase(holder.viewBind, position)
        } else {
            initViewCase(holder.viewBind, position, payloads)
        }
    }

    open fun initViewCase(binding: T, position: Int, payloads: MutableList<Any>){}

    open fun initViewCase(binding: T, position: Int){}
}