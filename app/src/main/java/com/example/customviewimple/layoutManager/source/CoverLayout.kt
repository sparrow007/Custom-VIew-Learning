package com.example.customviewimple.layoutManager.source

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Rect
import android.util.SparseArray
import android.util.SparseBooleanArray
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.math.sqrt

/**
 * Initial layout manager for just showing the view in the manner
 * 1. NO Infinite loop of items (done)
 * 2. NO support for drawing order in overlapping of child view (done)
 * 3. No implementation of fix position when scroll (done)
 * 4. No implementation of the animation in the layout manager (done)
 * 5. Create attributes for the user
 * 6. Create few callback on selected items so that user has easy access for center position
 * 7. Implementation of alpha features
 */

class CoverLayout constructor(
    isLoop: Boolean, isItem3D: Boolean, ratio: Float, flat: Boolean, alpha: Boolean)
    : RecyclerView.LayoutManager() {

    /**
     * We are supposing that all the items in recyclerview will have same size
     * Decorated child view width
     * */
    private var mItemDecoratedWidth: Int = 0

    /** Decorated child view height */
    private var mItemDecoratedHeight: Int = 0

    /** Initially position of an item (x coordinates) */
    private var mStartX = 0

    /** items Sliding offset */
    private var mOffsetAll = 0

    /** interval ratio is how much portion of a view will show */
    private var intervalRatio = 0.5f

    /** Cached all required items in rect object (left, top, right, bottom) */
    private val mAllItemsFrames = SparseArray<Rect>()

    /** Cache those items which are currently attached to the screen */
    private val mHasAttachedItems = SparseBooleanArray()

    /** animator animate the items in layout manager */
    private var animator:ValueAnimator?= null

    /** Store recycler so that we can use it in [scrollToPosition]*/
    private lateinit var recycler: RecyclerView.Recycler

    /** Store state so that we use in scrolling [scrollToPosition] */
    private lateinit var state: RecyclerView.State

    /** set infinite loop of items in the layout manager if true */
    private var mInfinite = false

    /** set tilt of items in layout manager if true */
    private var is3DItem = false

    /** set flat each item if true */
    private var isFlat = false

    /** set alpha based on the position of items in layout manager if true */
    private var isAlpha = false

    /** interface for the selected item or middle item in the layout manager */
    private var mSelectedListener: OnSelected? = null

    /** selected position of layout manager (center item)*/
    private var selectedPosition: Int = 0

    /** Previous item which was in center in layout manager */
    private var mLastSelectedPosition: Int = 0

    /** Initialize all the attribute from the constructor and also apply some conditions */
    init {
        this.mInfinite = isLoop
        this.is3DItem = isItem3D
        this.isAlpha = alpha
        if (ratio in 0f..1f) this.intervalRatio = ratio
        isFlat = flat
        if (isFlat) intervalRatio = 1.1f
    }

    companion object {
        /**Item moves to right */
        private const val SCROLL_TO_RIGHT = 1

        /**Items moves to left */
        private const val SCROLL_TO_LEFT = 2

        /**
         * Maximum information that can store at a time so if there are suppose more than 10000
         * it is not good idea to cache those views so initially we keep 100 items in cache memory
         */
        private const val MAX_RECT_COUNT = 100

    }

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.WRAP_CONTENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        if (state == null || recycler == null)
            return

        if (state.itemCount <= 0 || state.isPreLayout) {
            mOffsetAll = 0
            return
        }

        mAllItemsFrames.clear()
        mHasAttachedItems.clear()

        val scrap = recycler.getViewForPosition(0)
        addView(scrap)
        measureChildWithMargins(scrap, 0, 0)

        mItemDecoratedWidth = getDecoratedMeasuredWidth(scrap)
        mItemDecoratedHeight = getDecoratedMeasuredHeight(scrap)
        mStartX = ((getHorizontalSpace() - mItemDecoratedWidth) * 1.0f / 2).roundToInt()

        var offset = mStartX

        //Start from the center of the recyclerview
        //Save only specific item position
        var i = 0
        while (i < itemCount && i < MAX_RECT_COUNT) {
            var frame = mAllItemsFrames[i]
            if (frame == null) {
                frame = Rect()
            }
            frame.set(offset, 0, (offset + mItemDecoratedWidth), mItemDecoratedHeight)
            mAllItemsFrames.put(i, frame)
            mHasAttachedItems.put(i, false)
            offset += getIntervalDistance()
            i++
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
        if (animator != null && animator!!.isRunning) {
            animator?.cancel()
        }

        if (recycler == null || state == null) return 0

        var travel = dx

      if (!mInfinite) {
          if (dx + mOffsetAll < 0) {
              travel = - mOffsetAll
          }else if (dx + mOffsetAll > maxOffset()) {
              travel = (maxOffset() - mOffsetAll)
          }
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

        if (state.isPreLayout) return
        val displayFrames = Rect(
            mOffsetAll,
            0,
            mOffsetAll + getHorizontalSpace(),
            getVerticalSpace()
        )
        var position = 0
        for (index in 0 until childCount) {

            val child = getChildAt(index) ?: break
            position = if (child.tag != null) {
                //get position from tag class define later
                val tag = checkTAG(child.tag)
                tag!!.pos
            }else {
                getPosition(child)
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

        if (position == 0) position = centerPosition()
        /**
         * For making infinite loop for the layout manager
         */
        var min = position - 20
        var max = position + 20

        if (!mInfinite) {
            if (min < 0) min = 0
            if (max > itemCount ) max = itemCount
        }

        for (index in min until max) {
            val rect = getFrame(index)
           if (Rect.intersects(displayFrames, rect) && !mHasAttachedItems.get(
                   index
               )) {
               var actualPos = index % itemCount
               if (actualPos < 0) actualPos += itemCount

               val scrap = recycler.getViewForPosition(actualPos)
               checkTAG(scrap.tag)
               scrap.tag = TAG(index)
               measureChildWithMargins(scrap, 0, 0)

               if (scrollToDirection == SCROLL_TO_RIGHT) {
                   addView(scrap, 0)
               } else addView(scrap)

               layoutItem(scrap, rect)
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

        if (!isFlat) {
            child.scaleX = computeScale(rect.left - mOffsetAll)
            child.scaleY = computeScale(rect.left - mOffsetAll)
        }

        if (is3DItem) itemRotate(child, rect)

        if (isAlpha) child.alpha = computeAlpha(rect.left - mOffsetAll)

    }

    private fun itemRotate(child: View, frame: Rect) {
        val itemCenter = (frame.left + frame.right - 2*mOffsetAll) / 2f
        var value = (itemCenter - (mStartX + mItemDecoratedWidth / 2f)) * 1f / (itemCount*getIntervalDistance())
        value = sqrt(abs(value).toDouble()).toFloat()
        val symbol =
            if (itemCenter > mStartX + mItemDecoratedWidth / 2f) (-1).toFloat() else 1.toFloat()
        child.rotationY = symbol * 50* abs(value)

    }

    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        when (state) {
            RecyclerView.SCROLL_STATE_IDLE -> {
                //When scrolling stops
                fixOffsetWhenFinishOffset()
            }

            RecyclerView.SCROLL_STATE_DRAGGING -> {
            }
            RecyclerView.SCROLL_STATE_SETTLING -> {
            }
        }
    }

    private fun fixOffsetWhenFinishOffset() {
        if (getIntervalDistance() != 0) {
            var scrollPosition = (mOffsetAll * 1.0f / getIntervalDistance()).toInt()
            val moreDx: Float = (mOffsetAll % getIntervalDistance()).toFloat()
            if (abs(moreDx) > getIntervalDistance() * 0.5f) {
                if (moreDx > 0) scrollPosition++
                else scrollPosition--
            }
            val finalOffset = scrollPosition * getIntervalDistance()
            startScroll(mOffsetAll, finalOffset)
        }
    }

    private fun startScroll(from: Int, to: Int) {
        //Start animation
        if (animator != null && animator!!.isRunning) {
            animator?.cancel()
        }
        val direction = if (from < to) SCROLL_TO_LEFT else SCROLL_TO_RIGHT

        animator = ValueAnimator.ofFloat(from * 1.0f, to * 1.0f)
        animator?.duration= 500
        animator?.interpolator = DecelerateInterpolator()

        animator?.addUpdateListener { animation ->
            mOffsetAll = (animation.animatedValue as Float).roundToInt()
            layoutItems(recycler, state, direction)
        }
        animator?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                onSelectedCallback()
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }

        })
        animator?.start()
    }

    override fun scrollToPosition(position: Int) {
        if (position < 0 || position > itemCount - 1) return
        mOffsetAll = calculateOffset(position)
        layoutItems(recycler,
            state,
            if (position > selectedPosition) SCROLL_TO_LEFT
            else SCROLL_TO_RIGHT)
        onSelectedCallback()

    }

    override fun smoothScrollToPosition(
        recyclerView: RecyclerView?,
        state: RecyclerView.State?,
        position: Int
    ) {
        //Loop does not support for smooth scrolling
        if (mInfinite) return
        val finalOffset = calculateOffset(position)
        startScroll(mOffsetAll, finalOffset)
    }

    fun centerPosition(): Int {
        var pos = mOffsetAll / getIntervalDistance()
        val more = mOffsetAll % getIntervalDistance()
        if (abs(more) >= getIntervalDistance() * 0.5f) {
            if (more >= 0) pos++
            else pos--
        }
        return pos
    }

    fun getChildActualPos(index: Int): Int {
        val child = getChildAt(index)
        if (child!!.tag != null) {
            val tag = checkTAG(child.tag)
            if (tag != null)
            return tag.pos
        }
        return getPosition(child)
    }

    private fun checkTAG(tag: Any?): TAG? {
        return if (tag != null) {
            if (tag is TAG) {
                tag as TAG
            }else {
                throw IllegalArgumentException("You should use the set tag with the position")

            }
        }else {
            null
        }

    }

    override fun onAdapterChanged(
        oldAdapter: RecyclerView.Adapter<*>?,
        newAdapter: RecyclerView.Adapter<*>?
    ) {
        removeAllViews()
        mOffsetAll = 0
        mHasAttachedItems.clear()
        mAllItemsFrames.clear()
    }

    /**
     * Get the first visible position from the layout manager
     * @return get view which is at the start of the screen
     */
    fun getFirstVisiblePosition(): Int {
        val displayFrame =
            Rect(mOffsetAll, 0, mOffsetAll + getHorizontalSpace(), getVerticalSpace())
        val cur: Int = centerPosition()
        var i = cur - 1
        while (true) {
            val rect = getFrame(i)
            if (rect.left <= displayFrame.left) {
                return abs(i) % itemCount
            }
            i--
        }
    }

    /**
     * Get the last visible position from the layout manager
     * @return get view which is at the end of the screen
     */
    fun getLastVisiblePosition(): Int {
        val displayFrame =
            Rect(mOffsetAll, 0, mOffsetAll + getHorizontalSpace(), getVerticalSpace())
        val cur: Int = centerPosition()
        var i = cur - 1
        while (true) {
            val rect = getFrame(i)
            if (rect.left >= displayFrame.left) {
                return abs(i) % itemCount
            }
            i++
        }
    }

    private fun onSelectedCallback() {
        selectedPosition = ((mOffsetAll / getIntervalDistance()).toFloat()).roundToInt()

        if (selectedPosition < 0) selectedPosition += itemCount
        selectedPosition = abs(selectedPosition % itemCount)
        //check if the listener is implemented
        //mLastSelectedPosition keeps track of last position which will prevent simple slide and same position
        if (mSelectedListener != null && selectedPosition != mLastSelectedPosition) {
            mSelectedListener!!.onItemSelected(selectedPosition)
        }
        mLastSelectedPosition = selectedPosition
    }


    private fun getFrame(position: Int): Rect {
        var frame = mAllItemsFrames[position]
        if (frame == null) {
            frame = Rect()
            val offset = mStartX + getIntervalDistance() * position
            frame.set((offset), 0, (offset + mItemDecoratedWidth), mItemDecoratedHeight)
            return frame
        }
        return frame
    }
    private fun computeScale(x: Int): Float {
        var scale: Float =
            1 - abs(x - mStartX) * 1.0f / abs(mStartX + mItemDecoratedWidth / intervalRatio)

        if (scale < 0) scale = 0f
        if (scale > 1) scale = 1f
        return scale
    }

    private fun computeAlpha(x: Int): Float {
        var alpha: Float =
            1 - abs(x - mStartX) * 1.0f / abs(mStartX + mItemDecoratedWidth / intervalRatio)

        if (alpha < 0.3f) alpha = 0f
        if (alpha > 1) alpha = 1f
        return alpha
    }

    private fun calculateOffset(position: Int): Int {
        return ((getIntervalDistance() * position).toFloat()).roundToInt()
    }

    private fun getIntervalDistance(): Int {
        return (mItemDecoratedWidth * intervalRatio).roundToInt()
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

    interface OnSelected {
        fun onItemSelected(position: Int)
    }

    fun setOnSelectedListener(l: OnSelected) {
        this.mSelectedListener = l
    }

    /**
     * Get the selected position or centered position
     * @return selectedPosition
     */
    internal fun getSelectedPosition() = selectedPosition

    /**
     * Use the builder pattern to get all the required attribute for the layout manager
     */
    class Builder {
        private var isInfinite = false
        private var is3DItem = false
        private var intervalRation: Float = 0.5f
        private var isFlat = false
        private var isAlpha = false

        fun setIsInfinite(isInfinite: Boolean) : Builder {
            this.isInfinite = isInfinite
            return this
        }

        fun set3DItem(is3DItem: Boolean): Builder {
            this.is3DItem = is3DItem
            return this
        }

        fun setIntervalRatio(intervalRatio: Float): Builder {
            this.intervalRation = intervalRatio
            return this
        }

        fun setIsFlat(isFlat: Boolean): Builder {
            this.isFlat = isFlat
            return this
        }

        fun setIsAlpha(isAlpha: Boolean): Builder {
            this.isAlpha = isAlpha
            return this
        }

        fun build(): CoverLayout {
            return CoverLayout(isInfinite, is3DItem, intervalRation, isFlat, isAlpha)
        }
    }

    /**
     * Store the child position for laying out the view
     */
    internal data class TAG(var pos: Int = 0)

}