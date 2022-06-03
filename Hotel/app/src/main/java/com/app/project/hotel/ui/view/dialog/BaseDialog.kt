package com.app.project.hotel.ui.view.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.app.project.hotel.R
import com.app.project.hotel.databinding.DialogBaseLayoutBinding

open class BaseDialog<T: ViewDataBinding>(context: Context, @LayoutRes layoutId: Int, val cancelable: Boolean): AppCompatDialog(context) {
    val base: DialogBaseLayoutBinding by lazy {
        DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_base_layout, window?.decorView as? ViewGroup, false)
    }

    val binding: T by lazy {
        DataBindingUtil.inflate<T>(LayoutInflater.from(context), layoutId, base.layoutOutSide, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setWindowAnimations(R.style.bottom_in_bottom_out_anim)

        requestWindowFeature(Window.FEATURE_NO_TITLE)

        val lp = FrameLayout.LayoutParams(binding.root.layoutParams)
        lp.gravity = Gravity.CENTER

        base.layoutOutSide.addView(binding.root, lp)

        setContentView(base.root)
        window?.apply {
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS) //包括系统栏
        }
    }

    override fun onStart() {
        super.onStart()
        setCanceledOnTouchOutside(true)
    }
}