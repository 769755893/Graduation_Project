package com.app.project.hotel.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.TextView

@SuppressLint("AppCompatCustomView")
class AutoSizeTextView: TextView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val line = lineCount
        var mTextSize = textSize
        if (line > 1) {
            mTextSize --
            setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize)
        }
    }
}