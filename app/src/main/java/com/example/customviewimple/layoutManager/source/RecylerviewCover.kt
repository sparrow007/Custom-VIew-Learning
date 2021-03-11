package com.example.customviewimple.layoutManager.source

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import com.example.customviewimple.layoutManager.coverlayout.CoverFlowLayoutManger

/**
 * Simple implementation of the recyclerview in android
 * No support of the drawing order of the children in the recyclerView (done almost)
 * NO understanding of the dispatch event for the touch (Not done)
 */

class RecylerviewCover(context: Context, attributeSet: AttributeSet) : RecyclerView(
    context,
    attributeSet
) {

     private var mDownX:Float = 0.0f

    init {
        //layoutManager = CoverFlowLayoutManger(false, false, false, -1f,true, false)
        layoutManager = CoverLayout()
        isChildrenDrawingOrderEnabled = true
       // overScrollMode = OVER_SCROLL_NEVER

    }

     fun getCoverLayoutManager(): CoverFlowLayoutManger {
        return layoutManager as CoverFlowLayoutManger
    }

     fun getCoverLayout(): CoverLayout {
        return layoutManager as CoverLayout
    }
    override fun getChildDrawingOrder(childCount: Int, i: Int): Int {
        val center: Int = getCoverLayout().centerPosition()

        // 获取 RecyclerView 中第 i 个 子 view 的实际位置
        // Get the actual position of the i-th child view in RecyclerView

        // 获取 RecyclerView 中第 i 个 子 view 的实际位置
        // Get the actual position of the i-th child view in RecyclerView
        val actualPos: Int = getCoverLayout().getChildActualPos(i)

        // 距离中间item的间隔数
        // The number of intervals from the middle item

        // 距离中间item的间隔数
        // The number of intervals from the middle item
        val dist = actualPos - center

//        Log.e("MY TAG", "CENTER $center")
//        Log.e("MY TAG", "ACTUAL POS $actualPos")
//        Log.e("MY TAG", "dist $dist")
//        Log.e("MY TAG", "child count $childCount")

        var order: Int
        // [< 0] indicates that the item is located to the left of the middle item and can be drawn in order
        // [< 0] indicates that the item is located to the left of the middle item and can be drawn in order
        order = if (dist < 0) { // [< 0] 说明 item 位于中间 item 左边，按循序绘制即可
            i
        } else { // [>= 0] 说明 item 位于中间 item 右边，需要将顺序颠倒绘制
            //[>= 0] It means that the item is located to the right
            // of the middle item, and the order needs to be reversed.
            childCount - 1 - dist
        }

        if (order < 0) order = 0 else if (order > childCount - 1) order = childCount - 1

       // Log.e("MY TAG", "order $order")

        return order
    }

//    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
//        when (ev.action) {
//            MotionEvent.ACTION_DOWN -> {
//                mDownX = ev.x
//                //Set the parent class not to intercept sliding events
//                parent.requestDisallowInterceptTouchEvent(true) //设置父类不拦截滑动事件
//            }
//            MotionEvent.ACTION_MOVE -> if (ev.x > mDownX && getCoverLayoutManager().centerPosition == 0 ||
//                ev.x < mDownX && getCoverLayoutManager().centerPosition ==
//                getCoverLayoutManager().itemCount - 1
//            ) {
//                //如果是滑动到了最前和最后，开放父类滑动事件拦截
//                //If it is sliding to the front and the end, open the parent sliding event interception
//                parent.requestDisallowInterceptTouchEvent(false)
//            } else {
//                //滑动到中间，设置父类不拦截滑动事件
//                //Slide to the middle, set the parent class not to intercept the sliding event
//                parent.requestDisallowInterceptTouchEvent(true)
//            }
//        }
//        return super.dispatchTouchEvent(ev)
//    }

}