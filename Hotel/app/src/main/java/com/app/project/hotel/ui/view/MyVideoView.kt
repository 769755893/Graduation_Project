package com.app.project.hotel.ui.view

import android.content.Context
import android.util.AttributeSet
import android.widget.VideoView
import androidx.core.text.HtmlCompat
import com.example.uitraning.util.Utils

class MyVideoView: VideoView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val w = Utils.getScreenWidth(context)
        val h = Utils.getScreenHeight(context)
        setMeasuredDimension(w + 100,h + 100)
    }
}