package com.example.uitraning.util.helper

import android.content.Context
import android.util.DisplayMetrics
import android.view.animation.DecelerateInterpolator
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import java.lang.reflect.Method

class ViewPager2SlowScrollHelper(private val vp: ViewPager2, var duration: Long) {
    private val recyclerView: RecyclerView
    private val mAccessibilityProvider: Any
    private val mScrollEventAdapter: Any
    private val onSetNewCurrentItemMethod: Method
    private val getRelativeScrollPositionMethod: Method
    private val notifyProgrammaticScrollMethod: Method

    init {
        val mRecyclerViewField = ViewPager2::class.java.getDeclaredField("mRecyclerView")
        mRecyclerViewField.isAccessible = true
        recyclerView = mRecyclerViewField.get(vp) as RecyclerView
        recyclerView.layoutManager
        val mAccessibilityProviderField =
            ViewPager2::class.java.getDeclaredField("mAccessibilityProvider")
        mAccessibilityProviderField.isAccessible = true
        mAccessibilityProvider = mAccessibilityProviderField.get(vp)
        onSetNewCurrentItemMethod =
            mAccessibilityProvider.javaClass.getDeclaredMethod("onSetNewCurrentItem")
        onSetNewCurrentItemMethod.isAccessible = true


        val mScrollEventAdapterField =
            ViewPager2::class.java.getDeclaredField("mScrollEventAdapter")
        mScrollEventAdapterField.isAccessible = true
        mScrollEventAdapter = mScrollEventAdapterField.get(vp)
        getRelativeScrollPositionMethod =
            mScrollEventAdapter.javaClass.getDeclaredMethod("getRelativeScrollPosition")
        getRelativeScrollPositionMethod.isAccessible = true

        notifyProgrammaticScrollMethod = mScrollEventAdapter.javaClass.getDeclaredMethod(
            "notifyProgrammaticScroll",
            Int::class.java,
            Boolean::class.java
        )
        notifyProgrammaticScrollMethod.isAccessible = true
    }

    fun setCurrentItem(item: Int) {
        var item = item
        val adapter: RecyclerView.Adapter<*> = vp.adapter as RecyclerView.Adapter<*>
        if (adapter.itemCount <= 0) {
            return
        }
        item = item.coerceAtLeast(0)
        item = item.coerceAtMost(adapter.itemCount - 1)
        if (item == vp.currentItem && vp.scrollState == ViewPager2.SCROLL_STATE_IDLE) {
            return
        }
        if (item == vp.currentItem) {
            return
        }
        vp.currentItem = item
        onSetNewCurrentItemMethod.invoke(mAccessibilityProvider)
        notifyProgrammaticScrollMethod.invoke(mScrollEventAdapter, item, true)
        smoothScrollToPosition(item, vp.context, recyclerView.layoutManager)
    }

    private fun smoothScrollToPosition(
        item: Int,
        context: Context,
        layoutManager: RecyclerView.LayoutManager?
    ) {
        val linearSmoothScroller = getSlowLinearSmoothScroller(context)
        replaceDecelerateInterpolator(linearSmoothScroller)
        linearSmoothScroller.targetPosition = item
        layoutManager?.startSmoothScroll(linearSmoothScroller)
    }

    private fun getSlowLinearSmoothScroller(context: Context): RecyclerView.SmoothScroller {
        return object : LinearSmoothScroller(context) {
            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {
                return duration/(vp.width.toFloat()*3.0f)
            }
        }
    }

    private fun replaceDecelerateInterpolator(linearSmoothScroller: RecyclerView.SmoothScroller) {
        val mDecelerateInterpolatorField =
            LinearSmoothScroller::class.java.getDeclaredField("mDecelerateInterpolator")
        mDecelerateInterpolatorField.isAccessible = true
        mDecelerateInterpolatorField.set(linearSmoothScroller, object : DecelerateInterpolator() {
            override fun getInterpolation(input: Float): Float {
                return input
            }
        })
    }
}
