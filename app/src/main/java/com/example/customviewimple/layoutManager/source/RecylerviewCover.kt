package com.example.customviewimple.layoutManager.source

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

/**
 * Simple implementation of the recyclerview in android
 * No support of the drawing order of the children in the recyclerView
 * NO understanding of the dispatch event for the touch
 */

class RecylerviewCover(context: Context, attributeSet: AttributeSet) : RecyclerView(
    context,
    attributeSet
) {

     private var mDownX:Float = 0.0f

    private val layoutManager = CoverLayout()
    init {
        setLayoutManager(layoutManager)
        isChildrenDrawingOrderEnabled = true
        overScrollMode = OVER_SCROLL_NEVER

    }

    override fun getChildDrawingOrder(childCount: Int, i: Int): Int {

        val center = layoutManager.centerPosition()
        val pos = layoutManager.getChildActualPos(i)

        var order = 0
        val dis = pos - center

        order = if (dis < 0) {
            i
        }else childCount - 1 - dis

        if (order < 0) order = 0
        else if (order > childCount - 1) order = childCount - 1


        return order

    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                mDownX = ev.x
                //Set the parent class not to intercept sliding events
                parent.requestDisallowInterceptTouchEvent(true) //设置父类不拦截滑动事件
            }
            MotionEvent.ACTION_MOVE -> if (ev.x > mDownX && layoutManager.centerPosition() === 0 ||
                ev.x < mDownX && layoutManager.centerPosition() ===
                layoutManager.getItemCount() - 1
            ) {
                //如果是滑动到了最前和最后，开放父类滑动事件拦截
                //If it is sliding to the front and the end, open the parent sliding event interception
                parent.requestDisallowInterceptTouchEvent(false)
            } else {
                //滑动到中间，设置父类不拦截滑动事件
                //Slide to the middle, set the parent class not to intercept the sliding event
                parent.requestDisallowInterceptTouchEvent(true)
            }
        }
        return super.dispatchTouchEvent(ev)
    }

}