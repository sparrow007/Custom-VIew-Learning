package com.example.customviewimple.layoutManager.source

import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.graphics.Rect
import android.util.SparseArray
import android.util.SparseBooleanArray
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * Initial layout manager for just showing the view in the manner
 * 1. NO Infinite loop of items
 * 2. NO support for drawing order in overlapping of child view
 * 3. No implementation of fix position when scroll
 * 4. No implementation of the animation in the layout manager
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

    private var valueAnimator:ValueAnimator?= null
    private lateinit var recycler: RecyclerView.Recycler
    private lateinit var state: RecyclerView.State


    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.WRAP_CONTENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {

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
        this.recycler = recycler
        this.state = state
    }

    override fun canScrollHorizontally(): Boolean {
        return true
    }

    override fun scrollHorizontallyBy(
        dx: Int,
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?
    ): Int {

        if (recycler == null || state == null) return 0

        var travel = dx

        if (travel + mOffsetAll < 0) {
            travel = -mOffsetAll
        }else if (travel + mOffsetAll > maxOffset()) {
            travel = (maxOffset() - mOffsetAll)
        }

        mOffsetAll += travel
        layoutItems(recycler, state, if (dx > 0) SCROLL_TO_LEFT else SCROLL_TO_RIGHT)
        return travel
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
        for (index in 0 until childCount) {

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
                mHasAttachedItems.put(position, true)
            }
        }

       for (index in 0 until itemCount) {
           if (Rect.intersects(displayFrames, mAllItemsFrames.get(index)) && !mHasAttachedItems.get(
                   index
               )) {
               val scrap = recycler.getViewForPosition(index)
               measureChildWithMargins(scrap, 0, 0)

               if (scrollToDirection == SCROLL_TO_RIGHT) {
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

        child.scaleX = computeScale(rect.left - mOffsetAll)

        child.scaleY = computeScale(rect.left - mOffsetAll)

    }

    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        if (state == RecyclerView.SCROLL_STATE_IDLE) {
            //Fix the position of item when scroll state stops
            fixOffsetWhenFinishOffset()
        }
    }

    private fun fixOffsetWhenFinishOffset() {
        if (getIntervalDistance() != 0) {
            var scrollPosition = (mOffsetAll / getIntervalDistance())
            val moreDx = (mOffsetAll % getIntervalDistance()).toFloat()
            if (Math.abs(moreDx) > (getIntervalDistance()* 0.5f)) {
                if (moreDx > 0) scrollPosition++
                else scrollPosition--
            }
            val finalOffset = scrollPosition * getIntervalDistance()
            startScroll(mOffsetAll, finalOffset)
        }
    }

    private fun startScroll(from: Int, to: Int) {
        //Start animation
        if (valueAnimator != null && valueAnimator!!.isRunning) {
            valueAnimator?.cancel()
        }
        val direction = if (from < to) SCROLL_TO_LEFT else SCROLL_TO_RIGHT

        valueAnimator = ValueAnimator.ofFloat(from.toFloat(), to.toFloat())
        valueAnimator?.let {
            it.duration = 500
            it.interpolator = DecelerateInterpolator()
        }
        valueAnimator?.addUpdateListener(AnimatorUpdateListener { animation ->
            mOffsetAll = Math.round(animation.animatedValue as Float)
            layoutItems(recycler, state, direction)
        })
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
            1 - abs(x - mStartX) * 1.0f / abs(mStartX + mItemWidth / intervalRation)
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

    private fun maxOffset(): Int{
        return ((itemCount - 1) * getIntervalDistance())
    }

    override fun isAutoMeasureEnabled(): Boolean {
        return true
    }

}