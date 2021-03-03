package com.example.customviewimple.layoutManager

import android.animation.Animator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

class StackLayout() : RecyclerView.LayoutManager() {

    constructor(context: Context, viewGap: Int) : this() {
        this.normalViewGap = dp2px(context, viewGap.toFloat()).toInt()
        //Log.e("MY TAG", "NORAML GAP" + normalViewGap)
    }

    private fun dp2px(context: Context, dp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp,
            context.resources.displayMetrics
        )
    }

    private var onceCompleteScrollLength : Float = -1f
    private var normalViewGap : Int = 30
    private var mHorizontalScrollOffset : Long = 0

    private var firstVisiblePosition : Int = 0
    private var lastVisiblePosition : Int = 0

    private var childWidth = 0
    private var firstCompleteScrollLength : Float = 0f

    private var autoSelect = true
    private var animator : ValueAnimator? = null

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.WRAP_CONTENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {

        if (recycler == null || state == null) return
        if (state.itemCount == 0) {
            removeAndRecycleAllViews(recycler)
        }
        if (state.isPreLayout || !state.didStructureChange()) return

        onceCompleteScrollLength = -1f

        detachAndScrapAttachedViews(recycler)
        fill(recycler, 0)

    }

    override fun canScrollHorizontally(): Boolean {
        return true
    }

    override fun scrollHorizontallyBy(
        dx: Int,
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?
    ): Int {
         Log.e("MY TAG", "YOU ARE CALLING ME " + dx)

        if (dx == -1 || childCount == 0 || dx == 0) return 0


//        val realDx = dx / 1.0f
//        if (abs(realDx) < 0.00000001f) {
//            return 0
//        }

        mHorizontalScrollOffset += dx
        fill(recycler!!, dx.toLong())

        return dx
    }

    private fun fill(recycler: RecyclerView.Recycler, dx: Long) : Int{
        val movedOffset = fillHorizontallyLeft(recycler, dx)
        recycleChildView(recycler)
      //  Log.e("MY TAG", "MOVED " + movedOffset)
        return movedOffset
    }

    private fun fillHorizontallyLeft(recycler: RecyclerView.Recycler, dx: Long) : Int{

        var resultDx = dx

//        if (resultDx < 0) {
//            if (mHorizontalScrollOffset < 0) {
//                mHorizontalScrollOffset = 0
//                resultDx = 0
//            }
//        }
//
//        if (resultDx > 0) {
//            if (mHorizontalScrollOffset >= getMaxOffset()) {
//                mHorizontalScrollOffset = getMaxOffset().toLong()
//                resultDx = 0
//
//            }
//        }

        detachAndScrapAttachedViews(recycler)

        var startX = 0f
        var fraction = 0f

        var tempView : View? = null
        var tempPosition = 0

        if (onceCompleteScrollLength == -1f) {
            tempPosition = firstVisiblePosition
            tempView = recycler.getViewForPosition(tempPosition)
            measureChildWithMargins(tempView, 0, 0)
            childWidth = getDecoratedMeasurementHorizontal(tempView)
        }

        firstCompleteScrollLength = width/2f + childWidth/2f

        if (mHorizontalScrollOffset >= firstCompleteScrollLength) {
            startX = normalViewGap.toFloat()
            onceCompleteScrollLength = (childWidth + normalViewGap).toFloat()
            firstVisiblePosition = ((mHorizontalScrollOffset - firstCompleteScrollLength) / onceCompleteScrollLength).toInt() + 1
            fraction = abs((mHorizontalScrollOffset - firstCompleteScrollLength)) % onceCompleteScrollLength / (onceCompleteScrollLength*1.0f)
            Log.e("MY TAG", "CHILD FRACTION " + fraction)

        }else {
            startX =  -(childWidth+normalViewGap+normalViewGap).toFloat()
            onceCompleteScrollLength = firstCompleteScrollLength
            firstVisiblePosition = ((mHorizontalScrollOffset)/onceCompleteScrollLength).toInt() - 2

            Log.e("MY TAG", "Horizontal offset = "+ mHorizontalScrollOffset + " oncee " + onceCompleteScrollLength)

           fraction =  ((((mHorizontalScrollOffset) % onceCompleteScrollLength))) / onceCompleteScrollLength*1.0f

            Log.e("MY TAG", "FRACTION " + ((firstCompleteScrollLength) % onceCompleteScrollLength)/onceCompleteScrollLength)
            Log.e("MY TAG", "ANOTHER FRACTION " + fraction)
        }


        lastVisiblePosition = ((abs(mHorizontalScrollOffset) + getHorizontalSpace()) / (childWidth + normalViewGap)).toInt()
        val normalViewOffset = onceCompleteScrollLength * fraction

        Log.e("MY TAG", "nomal view offset = "+ normalViewOffset)


        var isNormalViewOffsetSet = false

        var i = firstVisiblePosition
        while (i <= lastVisiblePosition) {

            var recyclerIndex = i % itemCount
           // Log.e("MY TAG", "RECYLER INDEX " + recyclerIndex)
            if (recyclerIndex < 0) {
                recyclerIndex += itemCount
            }

            val item:View = if (recyclerIndex == tempPosition && tempView != null) {
                tempView
            }else {
                recycler.getViewForPosition(recyclerIndex)
            }

            if (i <= ((mHorizontalScrollOffset) / (childWidth + normalViewGap))) {
                addView(item)
            }else
                addView(item, 0)


            measureChildWithMargins(item, 0, 0)

            if (!isNormalViewOffsetSet) {
                startX -= normalViewOffset
                isNormalViewOffsetSet = true
            }

            val l: Int = startX.toInt()
            val r: Int = l + getDecoratedMeasurementHorizontal(item)
            val t: Int = paddingTop
            val b: Int = t + getDecoratedMeasurementVertical(item)

            val minScale = 0.7f
            val childCenterX = (r + l) / 2
            val parentCenterX = width / 2
            val childLayoutLeft = childCenterX <= parentCenterX

            val currentScale = if (childLayoutLeft) {
                val fractionScale = (parentCenterX - childCenterX).toFloat() / parentCenterX*1.0f
                1.0f - (1.0f - minScale) * fractionScale
            }else {
                val fractionScale = (childCenterX - parentCenterX).toFloat() / parentCenterX * 1.0f

              //  Log.e("MY TAG", "SCALE " + (childCenterX - parentCenterX).toFloat() / parentCenterX)

                1.0f - (1.0f - minScale) * fractionScale
            }
            item.scaleX = currentScale
            item.scaleY = currentScale
           // item.alpha = currentScale

            layoutDecorated(item, l, t, r, b)

            startX += childWidth + normalViewGap

//            if (startX > width - paddingRight) {
//                lastVisiblePosition = i
//                break
//            }

            i++
        }
        return mHorizontalScrollOffset.toInt()
    }

    private fun recycleChildView(recycler: RecyclerView.Recycler) {
        val list = recycler.scrapList

        var i = 0
        while (i < list.size) {
            removeAndRecycleView(list[i].itemView, recycler)
            i++
        }

    }

    private fun getDecoratedMeasurementHorizontal(view: View) : Int{
        val  viewParams = view.layoutParams as (RecyclerView.LayoutParams)
        return getDecoratedMeasuredWidth(view) + viewParams.leftMargin + viewParams.rightMargin
    }

    private fun getDecoratedMeasurementVertical(view: View) : Int {
        val  viewParams = view.layoutParams as (RecyclerView.LayoutParams)
        return getDecoratedMeasuredHeight(view) + viewParams.topMargin + viewParams.bottomMargin
    }

    private fun getMaxOffset() : Int {
        if (childWidth == 0 || itemCount == 0) return 0;
        return (childWidth + normalViewGap) * (itemCount - 1)
    }

    private fun getMinOffset() : Float {
        if (itemCount == 0) return 0f
        return (getHorizontalSpace() - childWidth) / 2f
    }

   private fun getHorizontalSpace(): Int {
        return width - paddingLeft - paddingRight
    }

    @Override
    override fun isAutoMeasureEnabled(): Boolean {
        return true
    }

    interface OnStackListener {
        fun onAnimationEnd()
    }


}