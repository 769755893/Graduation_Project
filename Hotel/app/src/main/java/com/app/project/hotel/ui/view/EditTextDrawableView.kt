package com.app.project.hotel.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.EditText

@SuppressLint("AppCompatCustomView")
class EditTextDrawableView : EditText {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    interface DrawableClick {
        fun rightDrawableClick()
    }

    var drawableClickCallBack: DrawableClick? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null)
            if (event.action == MotionEvent.ACTION_UP) {
                val drawable = compoundDrawables[2] ?: return super.onTouchEvent(event)
                val bound = drawable.bounds
                val drawableWidth = bound.right - bound.left
                val drawableHeight = bound.bottom - bound.top
                if (event.x >= width - paddingRight - drawableWidth && event.x <= width - paddingRight) {
                    if (event.y >= height / 2 - drawableHeight / 2 && event.y <= height / 2 + drawableHeight / 2) {
                        drawableClickCallBack?.rightDrawableClick()
                    }
                }
            }
        return super.onTouchEvent(event)
    }
}