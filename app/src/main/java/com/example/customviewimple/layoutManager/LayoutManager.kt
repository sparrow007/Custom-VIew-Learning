package com.example.customviewimple.layoutManager

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class LayoutManager : RecyclerView.LayoutManager() {

    private var mItemWidth = 0

    private var mItemHight = 0

    private var mChildScale = 0.5f

    private var mFirstPositiion = 0

    private var mLeftResult = 0

    private var mSpace = 60


    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
       return RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT)
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
        addView(view)
        measureChildWithMargins(view, 0 , 0)
        mItemWidth = getViewHorizontalSpace(view)
        mItemHight = getViewVerticalSpace(view)

        removeAndRecycleView(view, recycler)

        val offset = mLeftResult
        var visibleCount = getVisibleCount()
        if(state.itemCount < visibleCount) {
            visibleCount = state.itemCount
        }

        var lastPosition = mFirstPositiion + visibleCount - 1
        if(lastPosition > state.itemCount- 1)
            lastPosition = state.itemCount - 1

       fill(lastPosition, recycler, offset)

    }



    private fun fill( lastPosition : Int, recycler: RecyclerView.Recycler?, offset : Int) {
          var leftOffset = offset + paddingLeft
        for(i in mFirstPositiion until  lastPosition + 1) {
            val childView = recycler!!.getViewForPosition(i)
            addView(childView)
            measureChildWithMargins(childView, 0, 0)
            val childWidth = getViewHorizontalSpace(childView)
            val childHeight = getViewVerticalSpace(childView)
            layoutDecorated(childView, leftOffset,paddingTop, leftOffset + childWidth, paddingTop + childHeight)
            handleChildView(childView, leftOffset)
            leftOffset += childWidth
        }

    }

    private fun handleChildView(childView: View, left: Int) {
        val childScale = computeScale(left)

        Log.e("MY TAG", "CHILD SCALE " + childView.height)
        childView.scaleX = childScale
        childView.scaleY = childScale
        Log.e("MY TAG", " af CHILD SCALE " + translateY(scale = childScale))
        Log.e("MY TAG", " scale CHILD SCALE " + translateX(childScale, left))
        if(left > mItemWidth)
        childView.translationX = translateX(childScale, left)
        childView.translationY = translateY(childScale)
    }

    private fun computeScale(left : Int) : Float {
        if(left > mItemWidth.shr(1)) {
          return mChildScale
        }else {
            return 1f
        }
    }

    private fun translateX(scale: Float, left: Int) : Float{
        if(left > mItemWidth) {

            Log.e("MY TAG", "LEFT START X = $left itemWisth = $mItemWidth")

            val a = left / mItemWidth
            Log.e("MY TAG", " what is left value =  " +a)

            return - (a *((1-scale) * mItemWidth/2 - mSpace) + (a-1) * ((1-scale) * mItemWidth/2))
        }else {
            return 0f
        }
    }

    private fun translateY(scale : Float) : Float {
        return -(1-scale) * mItemHight/2
    }


    /*
    * Find the visible number of child view that will show on the layout
    * */
    fun getVisibleCount() : Int {
        val space = width - mItemWidth
        return Math.ceil((space / (mChildScale * mItemWidth) + 1).toDouble()).toInt()
    }

    @Override
    override fun isAutoMeasureEnabled(): Boolean {
        return true
    }

    fun getViewHorizontalSpace(childView : View) : Int {
        val layoutParams = childView.layoutParams as RecyclerView.LayoutParams
        return getDecoratedMeasuredWidth(childView) + layoutParams.leftMargin + layoutParams.rightMargin
    }

    fun getViewVerticalSpace(childView : View) : Int {
        val layoutParams =childView.layoutParams as RecyclerView.LayoutParams
        return getDecoratedMeasuredHeight(childView) + layoutParams.topMargin + layoutParams.bottomMargin
    }
}