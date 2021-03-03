package com.example.customviewimple.layoutManager.source

import android.graphics.Rect
import android.util.SparseArray
import android.util.SparseBooleanArray
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

/**
 * Initial layout manager for just showing the view in the manner
 * 1. NO Infinite loop of items
 * 2. NO support for the scrolling
 * 3. No Implementation of the layout manager
 * 4. No implementation of the animation in the layout manager
 *
 */

class CoverLayout: RecyclerView.LayoutManager() {

    private var mItemWidth: Int = 0
    private var mItemHeight: Int = 0

    private var mStartX = 0
    private var mStartY = 0

    private var mOffsetAll = 0

    private val MAX_RECT_COUNT = 100

    private val SCROLL_TO_RIGHT = 1
    private val SCROLL_TO_LEFT = 2

    private var intervalRation = 0.5f

    private val mAllItemsFrames = SparseArray<Rect>()

    private val mHasAttachedItems = SparseBooleanArray()


    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.WRAP_CONTENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        super.onLayoutChildren(recycler, state)

        if (state == null || recycler == null)
            return

        if (state.itemCount == 0 || state.isPreLayout || !state.didStructureChange()) {
            mOffsetAll = 0
            return
        }

        val scrap = recycler.getViewForPosition(0)
        addView(scrap)
        measureChildWithMargins(scrap, 0, 0)

        mItemWidth = getDecoratedMeasuredWidth(scrap)
        mItemHeight = getDecoratedMeasuredHeight(scrap)
        mStartX = ((getHorizontalSpace() - mItemWidth) * 1.0f / 2).roundToInt()

        var offset = mStartX

        //Start from the center of the recyclerview
        //Save only specific item position
        (0..itemCount).takeWhile { it < MAX_RECT_COUNT }.forEach { i ->

            var frame = mAllItemsFrames[i]
            if (frame == null) {
                frame = Rect()
            }
            frame.set(offset, 0, (offset + mItemWidth), mItemHeight)
            mAllItemsFrames.put(i, frame)
            mHasAttachedItems.put(i, false)
            offset += getIntervalDistance()
        }

        detachAndScrapAttachedViews(recycler)

        layoutItems(recycler, state, SCROLL_TO_LEFT)
    }

    /**
     * Layout out items
     * 1. First recycle those items views which are no longer visible to the screens
     * 2. Layout new items on the screens which are currently visible
     */

    private fun layoutItems(
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State,
        scrollToDirection: Int
    ) {

        val displayFrames = Rect(
            mOffsetAll,
            0,
            mOffsetAll + getHorizontalSpace(),
            getVerticalSpace()
        )
        var position = 0
        for (index in 0..childCount) {

            val child = getChildAt(index) ?: break
            if (child.tag != null) {
                //get position from tag class define later
            }else {
                position = getPosition(child)
            }

            val rect = getFrame(position)

            //Now check item is in the display area, if not recycle that item
            if (!Rect.intersects(displayFrames, rect)) {
                removeAndRecycleView(child, recycler)
                mHasAttachedItems.delete(position)
            }else {
                //Shift the item which has still in the screen
                layoutItem(child, rect)
                mHasAttachedItems.put(index, true)
            }
        }

       for (index in 0..itemCount) {
           if (Rect.intersects(displayFrames, mAllItemsFrames.get(index)) && !mHasAttachedItems.get(
                   index
               )) {
               val scrap = recycler.getViewForPosition(index)
               measureChildWithMargins(scrap, 0, 0)
               if (scrollToDirection == SCROLL_TO_LEFT) {
                   addView(scrap, 0)
               } else addView(scrap)
               layoutItem(scrap, mAllItemsFrames.get(index))
               mHasAttachedItems.put(index, true)
           }
       }


    }



    private fun layoutItem(child: View, rect: Rect) {
        layoutDecorated(
            child,
            rect.left - mOffsetAll,
            rect.top,
            rect.right - mOffsetAll,
            rect.bottom
        )

        //不是平面普通滚动的情况下才进行缩放
        child.scaleX = computeScale(rect.left - mOffsetAll) //缩放 Zoom

        child.scaleY = computeScale(rect.left - mOffsetAll) //缩放

    }

    private fun getFrame(position: Int): Rect {
        var frame = mAllItemsFrames[position]
        if (frame == null) {
            frame = Rect()
            val offset = mStartX + getIntervalDistance() * position
            frame.set(offset, 0, (offset + mItemWidth), mItemHeight)
            return frame
        }
        return frame
    }
    private fun computeScale(x: Int): Float {
        var scale: Float =
            1 - Math.abs(x - mStartX) * 1.0f / Math.abs(mStartX + mItemWidth / intervalRation)
        if (scale < 0) scale = 0f
        if (scale > 1) scale = 1f
        return scale
    }

    private fun getIntervalDistance(): Int {
        return (mItemWidth * intervalRation).roundToInt()
    }

    private fun getHorizontalSpace(): Int {
        return width - paddingLeft - paddingRight
    }

    private fun getVerticalSpace(): Int {
        return height - paddingBottom - paddingTop
    }

    override fun isAutoMeasureEnabled(): Boolean {
        return true
    }

}