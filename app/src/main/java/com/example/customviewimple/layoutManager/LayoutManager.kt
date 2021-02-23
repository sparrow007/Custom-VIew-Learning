package com.example.customviewimple.layoutManager

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.ceil

class LayoutManager : RecyclerView.LayoutManager() {

    private var mItemWidth = 0

    private var mItemHight = 0

    private var mChildScale = 0.6f
    private var mCoverScale = 0.8f

    private var mFirstPosition = 0

    private var mLeftResult = 0

    private var mSpace = 60
    private var sumDX = 0


    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
       return RecyclerView.LayoutParams(
           RecyclerView.LayoutParams.WRAP_CONTENT,
           RecyclerView.LayoutParams.WRAP_CONTENT
       )
    }

    @Override
    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        super.onLayoutChildren(recycler, state)

        if(state!!.itemCount == 0)
            removeAndRecycleAllViews(recycler!!)

        if(state.isPreLayout || !state.didStructureChange())
            return

        detachAndScrapAttachedViews(recycler!!)

        val view = recycler.getViewForPosition(0)
        measureChildWithMargins(view, 0, 0)
        mItemWidth = getDecoratedMeasuredWidth(view)
        mItemHight = getDecoratedMeasuredHeight(view)

        removeAndRecycleView(view, recycler)

        var offset = mLeftResult + paddingLeft
        var visibleCount = getVisibleCount(offset)
        if(state.itemCount < visibleCount) {
            visibleCount = state.itemCount
        }

        var lastPosition = mFirstPosition + visibleCount
        if(lastPosition > state.itemCount)
            lastPosition = state.itemCount

        for (i in 0 until lastPosition + 1) {
            insertView(i, recycler, offset)
            offset += mItemWidth
        }
    }

    private fun insertView(position: Int, recycler: RecyclerView.Recycler, left: Int) {
        Log.e("MY TAG", "LEFT = " + left)
        val childView = recycler.getViewForPosition(position)
        measureChildWithMargins(childView, 0, 0)
        addView(childView)
        val childWidth = getDecoratedMeasuredWidth(childView)
        val childHeight = getDecoratedMeasuredHeight(childView)
        layoutDecorated(childView, left, paddingTop, left + childWidth, paddingTop + childHeight)
        //handleChild(childView, left)
    }

    /**
     * Handle child scaling and its translation
     */
    private fun handleChild(childView: View, left: Int) {
        val scale = computeScale(left)
        childView.scaleX = scale
        childView.scaleY = scale
        childView.translationY = translateY(left)
        childView.translationX = -translateX(scale, left)
    }


    @Override
    override fun canScrollHorizontally(): Boolean {
        return true
    }

    override fun scrollHorizontallyBy(
        dx: Int,
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?
    ): Int {

        if (recycler == null || itemCount == 0) return 0

        Log.e("MY TAG", "SCROLL BY DX " + dx)
        var travel  = dx
        if (travel + sumDX < 0) {
            travel = -sumDX
        }else if (travel + sumDX > getMaxOffset()) {
            travel = getMaxOffset() - sumDX
        }
        sumDX += travel

       val left = mLeftResult
       var leftResult =  ((left - travel) % mItemWidth - mItemWidth) % mItemWidth + getPaddingLeft()
        mLeftResult = leftResult - paddingLeft


        var firstPos = mFirstPosition

        firstPos += -(left - travel)/mItemWidth

        removeView(travel, recycler, leftResult)

        detachAndScrapAttachedViews(recycler)

        mFirstPosition = firstPos
        for (i in firstPos until itemCount) {
            insertView(i, recycler, leftResult)
            leftResult += mItemWidth
        }

      //  offsetChildrenHorizontal(-resultOffset)

        return travel
    }


    private fun computeScale(left: Int) : Float {
        return if (left < -mItemWidth shr  1) {
            mCoverScale
        }
        else if (left < 0) {
            1 - (1 - mCoverScale) * (left + (mItemWidth shr  1))/ (mItemWidth shr 1)
        }
        else if (left < mItemWidth shr 1) {
            1f
        }
        else if (left < mItemWidth)
            1 - (1 - mChildScale) * (left - (mItemWidth shr  1))/ (mItemWidth shr 1)
        else
            mChildScale
    }

    private fun translateX(scale: Float, left: Int) : Float{
       return if (left < 0) {
           left + (1-scale) * (mItemWidth)/2
       }
       else if (left < mItemWidth shr 1)
           0f
        else if (left < mItemWidth) {
           (1-scale)*mItemWidth / 2 - (left / (mItemWidth shr 1) - 1) * mSpace
       }
        else {
           val a = left/mItemWidth
           a*(((1-scale) * mItemWidth) / 2 - mSpace) + (a - 1) *(((1-scale) * mItemWidth) / 2)
        }
    }

    private fun translateY(left: Int) : Float {
        return if (left > mItemWidth shr 1)
                  - ((1-mChildScale) * mItemHight) / 2
               else
                  0f
    }


    /*
    * Find the visible number of child view that will show on the layout
    * */
    private fun getVisibleCount(left: Int) : Int {
        if (left < 0) {
            val s = computeScale(left - paddingLeft + mItemWidth)
            val f = getHorizontalSpace() - (mSpace + mItemWidth) - (left - paddingLeft + mItemWidth)

        }
        val space = getHorizontalSpace() - mItemWidth
        return ceil((space / (mChildScale * mItemWidth)).toDouble()).toInt()
    }

    @Override
    override fun isAutoMeasureEnabled(): Boolean {
        return true
    }

    private fun getViewHorizontalSpace(childView: View) : Int {
        val layoutParams = childView.layoutParams as RecyclerView.LayoutParams
        return getDecoratedMeasuredWidth(childView)
    }

    private fun getViewVerticalSpace(childView: View) : Int {
        val layoutParams =childView.layoutParams as RecyclerView.LayoutParams
        return getDecoratedMeasuredHeight(childView)
    }

    private fun getHorizontalSpace() : Int {
        return width - paddingLeft - paddingRight
    }

    private fun removeView(dx: Int, recycler: RecyclerView.Recycler, left: Int) {
        if (dx > 0) {
            while (childCount > 0) {
                val child = getChildAt(0)
                val p = getDecoratedRight(child!!) - dx
                if (p <= 0)
                    removeAndRecycleView(child,recycler)
                else
                    break
            }
        }else {
            while (childCount > 0) {

            }
        }
    }

    /**
     * Maximum horizontal offset
     * @return maxOffset
     */
    private fun getMaxOffset(): Int {
        return (itemCount - 1) * mItemWidth
    }
}