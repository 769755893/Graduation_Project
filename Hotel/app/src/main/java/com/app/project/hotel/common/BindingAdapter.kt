package com.app.project.hotel.common

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.TypedValue
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.graphics.drawable.toDrawable
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import com.app.project.hotel.R
import com.bumptech.glide.Glide
import com.example.uitraning.util.Utils
import com.example.uitraning.util.log

@BindingAdapter(value = ["ImageViewSrc", "Width", "Height"], requireAll = false)
fun setImage(view: ImageView, ImageViewSrc: Int, Width: Int, Height: Int) {
    if (Width < 1)
        Glide.with(view.context.applicationContext)
            .load(ImageViewSrc)
            .into(view)
}

@BindingAdapter("filterBitmap")
fun changeBitmap(view: ImageView, filterBitmap: String?) {
    if (!filterBitmap.isNullOrEmpty()) {
        val de = Utils.string2Bitmap(filterBitmap)!!
        val drawable = Utils.resizeBitmap(de, 403, 690).toDrawable(view.resources)
        view.setImageDrawable(drawable)
    } else {
        view.setImageDrawable(
            ResourcesCompat.getDrawable(
                view.resources,
                R.drawable.ic_baseline_person_pin_24,
                view.resources.newTheme()
            )
        )
    }
}

@BindingAdapter("setText", "setText1")
fun setText(view: TextView, setText: Int, setText1: Boolean) {
    if (setText == 0) {
        if (setText1)
            view.text = "待审核"
        else
            view.text = "未上传"
    } else if (setText == 1) {
        view.text = "已发布"
    } else if (setText == 2) {
        view.text = "审核不通过, 请检查"
    }
}


@BindingAdapter("upLoad", "publish")
fun setStatus(view: TextView, upLoad: Boolean, publish: Int) {
    if (!upLoad || publish == 2) {
        view.isLongClickable = true
        view.isClickable = true
    } else {
        view.isLongClickable = false
        view.isClickable = false
    }

    if (upLoad) {
        view.background = ResourcesCompat.getDrawable(
            view.resources,
            R.drawable.bg_keep_btn_round_keeping,
            view.resources.newTheme()
        )
        if (publish == 2)
            view.text = "重新上传"
        else
            view.text = "已上传"
        view.setTextColor(Color.WHITE)
    } else {
        view.background = ResourcesCompat.getDrawable(
            view.resources,
            R.drawable.bg_keep_btn_round_unkeep,
            view.resources.newTheme()
        )
        view.text = "上传"
        view.setTextColor(Color.rgb(0xF5, 0xA6, 0x23))
    }
}

@BindingAdapter("roundDrawable")
fun setRoundDrawable(view: ImageView?, roundDrawable: String?) {
    if (view != null) {
        if (!roundDrawable.isNullOrEmpty()) {
            val rd = RoundedBitmapDrawableFactory.create(
                view.resources,
                Utils.string2Bitmap(roundDrawable)
            )
            rd.isCircular = true
            rd.cornerRadius = 250f
            view.background = ColorDrawable(Color.TRANSPARENT)
            view.setImageDrawable(rd)
        }
    }
}

@BindingAdapter("textSpannerString")
fun setSpannerString(view: TextView, textSpannerString: Int?) {
    if (textSpannerString != null) {
        view.text = HtmlCompat.fromHtml(
            "共<font color = #ff0000>$textSpannerString</font> 天",
            HtmlCompat.FROM_HTML_MODE_COMPACT
        )
    } else {
        view.text = HtmlCompat.fromHtml(
            "共<font color = #ff0000>" + 0 + "</font> 天",
            HtmlCompat.FROM_HTML_MODE_COMPACT
        )
    }
}

@BindingAdapter("orderStatus")
fun setOrderStatus(view: TextView, orderStatus: Int) {
    "orderStatus".log()
    when (orderStatus) {
        -1 -> view.text = HtmlCompat.fromHtml(
            "订单状态: <font color = #ff0000>用户已取消</font>",
            HtmlCompat.FROM_HTML_MODE_COMPACT
        )
        0 -> view.text = HtmlCompat.fromHtml(
            "订单状态: <font color = #ff0000>已支付待确认</font>",
            HtmlCompat.FROM_HTML_MODE_COMPACT
        )
        1 -> view.text = HtmlCompat.fromHtml(
            "订单状态: <font color = #ff0000>已确认</font>",
            HtmlCompat.FROM_HTML_MODE_COMPACT
        )
        2 -> view.text = HtmlCompat.fromHtml(
            "订单状态: <font color = #ff0000>未同意</font>",
            HtmlCompat.FROM_HTML_MODE_COMPACT
        )
        3 -> view.text = HtmlCompat.fromHtml(
            "订单状态: <font color = #ff0000>已完成</font>",
            HtmlCompat.FROM_HTML_MODE_COMPACT
        )
        4 -> view.text = HtmlCompat.fromHtml(
            "订单状态: <font color = #ff0000>已完成, 已评论</font>",
            HtmlCompat.FROM_HTML_MODE_COMPACT
        )
    }
}

@BindingAdapter("goodCnt")
fun setText(view: TextView, goodCnt: Int?) {
    if (goodCnt != null && view.width > 0) {
        view.text = goodCnt.toString()
    }
}

@InverseBindingAdapter(attribute = "goodCnt")
fun getText(view: TextView): Int {
    return view.text.toString().toInt()
}

@BindingAdapter("strDrawable")
fun strBitmap(view: ImageView, strDrawable: String?) {
    if (!strDrawable.isNullOrEmpty()) {
        view.setImageBitmap(Utils.string2Bitmap(strDrawable))
    }
}