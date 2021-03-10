package com.example.customviewimple.layoutManager.coverlayout


import android.animation.Animator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import android.util.SparseArray
import android.util.SparseBooleanArray
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.recyclerview.widget.RecyclerView


/**
 * Cover Flow布局类
 *
 * 通过重写LayoutManger布局方法[.onLayoutChildren]
 * 对Item进行布局，并对超出屏幕的Item进行回收
 *
 * 通过重写LayoutManger中的[.scrollHorizontallyBy]
 * 进行水平滚动处理
 *
 * Cover Flow layout class
 *
 * By rewriting the LayoutManger layout method [.onLayoutChildren]
 * Layout the items and recycle the items that exceed the screen
 *
 * By rewriting [.scrollHorizontallyBy] in LayoutManger
 * Perform horizontal scrolling
 *
 * @author Chen Xiaoping (562818444@qq.com)
 * @version V1.1：
 * 增加循环滚动功能
 *
 * @Datetime 2020-06-09
 */
class CoverFlowLayoutManger  constructor(
    isFlat: Boolean, isGreyItem: Boolean,
    isAlphaItem: Boolean, cstInterval: Float,
    isLoop: Boolean, is3DItem: Boolean
) :
    RecyclerView.LayoutManager() {
    /**滑动总偏移量 */
    /**Total sliding offset */
    private var mOffsetAll = 0
    /**Item宽 */
    /**Item width */
    private var mDecoratedChildWidth = 0
    /**Item高 */
    /**Item hight */
    private var mDecoratedChildHeight = 0
    /**Item间隔与item宽的比例 */
    /**The ratio of Item interval to item width */
    private var mIntervalRatio = 0.5f
    /**起始ItemX坐标 */
    /**Start ItemX coordinate */
    private var mStartX = 0
    /**起始Item Y坐标 */
    /**Start Item Y coordinate */
    private var mStartY = 0
    /**保存所有的Item的上下左右的偏移量信息 */
    /**Save the up, down, left, and right offset information of all items */
    private val mAllItemFrames = SparseArray<Rect>()
    /**记录Item是否出现过屏幕且还没有回收。true表示出现过屏幕上，并且还没被回收 */
    /** Record whether the Item has appeared on the screen and has not 4
     * yet been recycled. true means that it has appeared
     * on the screen and has not been recycled */
    private val mHasAttachedItems = SparseBooleanArray()
    /**RecyclerView的Item回收器 */
    /**Item recycler of RecyclerView */
    private var mRecycle: RecyclerView.Recycler? = null
    /**RecyclerView的状态器 */
    /**RecyclerView's state device */
    private var mState: RecyclerView.State? = null
    /**滚动动画 */
    /**Scrolling animation */
    private var mAnimation: ValueAnimator? = null
    /**正显示在中间的Item */
    /**
     * 获取被选中Item位置
     */
    /**Item being displayed in the middle */
    var selectedPos = 0
        private set
    /**前一个正显示在中间的Item */
    /**The previous Item that is displayed in the middle */
    private var mLastSelectPosition = 0
    /**选中监听 */
    /**Select monitor */
    private var mSelectedListener: OnSelected? = null
    /**是否为平面滚动，Item之间没有叠加，也没有缩放 */
    /**Whether it is a flat scroll, there is no overlap between items, and no zoom */
    private var mIsFlatFlow = false
    /**是否启动Item灰度值渐变 */
    /**Whether to start Item gray value gradient */
    private var mItemGradualGrey = false
    /**是否启动Item半透渐变 */
    /**Whether to activate Item semi-transparent gradient */
    private var mItemGradualAlpha = false
    /**是否无限循环 */
    /**Whether to loop infinitely */
    private var mIsLoop = false
    /**是否启动Item 3D 倾斜 */
    /**Whether to start Item 3D tilt */
    private var mItem3D = false
    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        //如果没有item，直接返回
        //跳过preLayout，preLayout主要用于支持动画

//If there is no item, return directly
        //Skip preLayout, preLayout is mainly used to support animation
        if (getItemCount() <= 0 || state.isPreLayout()) {
            mOffsetAll = 0
            return
        }
        mAllItemFrames.clear()
        mHasAttachedItems.clear()

        //得到子view的宽和高，这边的item的宽高都是一样的，所以只需要进行一次测量
        //Get the width and height of the subview, the width and
        // height of the item here are the same, so only one measurement is needed
        val scrap: View = recycler.getViewForPosition(0)
        addView(scrap)
        measureChildWithMargins(scrap, 0, 0)
        //计算测量布局的宽高
        //Calculate the width and height of the measurement layout
        mDecoratedChildWidth = getDecoratedMeasuredWidth(scrap)
        mDecoratedChildHeight = getDecoratedMeasuredHeight(scrap)
        mStartX = Math.round((horizontalSpace - mDecoratedChildWidth) * 1.0f / 2)
        mStartY = Math.round((verticalSpace - mDecoratedChildHeight) * 1.0f / 2)
        var offset = mStartX.toFloat()
        /**只存[MAX_RECT_COUNT]个item具体位置 */
        /**Save only [MAX_RECT_COUNT] specific item locations */
        var i = 0
        while (i < getItemCount() && i < MAX_RECT_COUNT) {
            var frame = mAllItemFrames[i]
            if (frame == null) {
                frame = Rect()
            }
            frame[Math.round(offset), mStartY, Math.round(offset + mDecoratedChildWidth)] =
                mStartY + mDecoratedChildHeight
            mAllItemFrames.put(i, frame)
            mHasAttachedItems.put(i, false)
            offset += intervalDistance //原始位置累加，否则越后面误差越大
            i++
        }

        //Before layout, Detach all the child views and put them in the Scrap cache
        detachAndScrapAttachedViews(recycler) //在布局之前，将所有的子View先Detach掉，放入到Scrap缓存中


        //Call smoothScrollToPosition or scrollToPosition before initialization, only the position will be recorded
        if ((mRecycle == null || mState == null) &&  //在为初始化前调用smoothScrollToPosition 或者 scrollToPosition,只会记录位置
            selectedPos != 0
        ) {                 //所以初始化时需要滚动到对应位置
            // //So you need to scroll to the corresponding position during initialization
            mOffsetAll = calculateOffsetForPosition(selectedPos)
            onSelectedCallBack()
        }
        layoutItems(recycler, state, SCROLL_TO_LEFT)
        mRecycle = recycler
        mState = state
    }

    override fun scrollHorizontallyBy(
        dx: Int, recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?
    ): Int {
        if (mAnimation != null && mAnimation!!.isRunning) mAnimation!!.cancel()
        var travel = dx
        if (!mIsLoop) { //非循环模式，限制滚动位置
            //Acyclic mode, limit the scroll position
            if (dx + mOffsetAll < 0) {
                travel = -mOffsetAll
            } else if (dx + mOffsetAll > maxOffset) {
                travel = (maxOffset - mOffsetAll).toInt()
            }
        }
        mOffsetAll += travel //累计偏移量
        layoutItems(recycler, state, if (dx > 0) SCROLL_TO_LEFT else SCROLL_TO_RIGHT)
        return travel
    }

    /**
     * 布局Item   Layout Item
     *
     *
     * 1，先清除已经超出屏幕的item
     * 1. First, clear the items that have exceeded the screen
     *
     * 2，再绘制可以显示在屏幕里面的item
     * 2. Then draw the item that can be displayed on the screen
     */
    private fun layoutItems(
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?, scrollDirection: Int
    ) {
        if (state == null || state.isPreLayout()) return
        val displayFrame = Rect(
            mOffsetAll, 0, mOffsetAll + horizontalSpace,
            verticalSpace
        )
        var position = 0
        for (i in 0 until childCount) {
            val child: View = getChildAt(i) ?: break
            if (child.tag != null) {
                val tag = checkTag(child.tag)
                position = tag!!.pos
            } else {
                position = getPosition(child)
            }
            val rect = getFrame(position)

            ////Item is not in the display area, it means that it needs to be recycled
            if (!Rect.intersects(displayFrame, rect)) { //Item没有在显示区域，就说明需要回收
                if (recycler != null) {
                    removeAndRecycleView(
                        child,
                        recycler
                    )
                } //回收滑出屏幕的View //Recycle the View that slides off the screen
                mHasAttachedItems.delete(position)
            } else { //Item还在显示区域内，更新滑动后Item的位置
                //Item is still in the display area, update the position of the item after sliding
                layoutItem(child, rect) //更新Item位置
                mHasAttachedItems.put(position, true)
            }
        }
        if (position == 0) position = centerPosition

        // 检查前后 20 个 item 是否需要绘制
        // Check whether the 20 items before and after need to be drawn
        var min = position - 20
        var max = position + 20
        if (!mIsLoop) {
            if (min < 0) min = 0
            if (max > getItemCount()) max = getItemCount()
        }
        for (i in min until max) {
            val rect = getFrame(i)
            if (Rect.intersects(displayFrame, rect) &&
                !mHasAttachedItems[i]
            ) { //重新加载可见范围内的Item //Reload the item in the visible range
                // 循环滚动时，计算实际的 item 位置
                // Calculate the actual item position when scrolling through the loop
                var actualPos: Int = i % itemCount
                // 循环滚动时，位置可能是负值，需要将其转换为对应的 item 的值
                // When scrolling in a loop, the position may be a negative value,
                // which needs to be converted to the value of the corresponding item
                if (actualPos < 0) actualPos = getItemCount() + actualPos
                val scrap: View? = recycler?.getViewForPosition(actualPos)
                checkTag(scrap!!.tag)
                scrap.tag = TAG(i)
                measureChildWithMargins(scrap, 0, 0)
                if (scrollDirection == SCROLL_TO_RIGHT || mIsFlatFlow) { //item 向右滚动，新增的Item需要添加在最前面
                    //item scroll to the right, the new item needs to be added at the top
                    addView(scrap, 0)
                } else { //item 向左滚动，新增的item要添加在最后面
                    //item scroll to the left, the new item should be added at the end
                    addView(scrap)
                }
                // Layout this Item
                layoutItem(scrap, rect) //将这个Item布局出来
                mHasAttachedItems.put(i, true)
            }
        }
    }

    /**
     * 布局Item位置
     * Layout Item position
     * @param child 要布局的Item
     * @param frame 位置信息
     */
    private fun layoutItem(child: View, frame: Rect) {
        layoutDecorated(
            child,
            frame.left - mOffsetAll,
            frame.top,
            frame.right - mOffsetAll,
            frame.bottom
        )
        //Scaling is only performed when it is not a plane ordinary scrolling
        if (!mIsFlatFlow) { //不是平面普通滚动的情况下才进行缩放
            child.scaleX = computeScale(frame.left - mOffsetAll) //缩放 Zoom
            child.scaleY = computeScale(frame.left - mOffsetAll) //缩放
        }
        if (mItemGradualAlpha) {
            child.alpha = computeAlpha(frame.left - mOffsetAll)
        }
        if (mItemGradualGrey) {
            greyItem(child, frame)
        }
        if (mItem3D) {
            item3D(child, frame)
        }
    }

    /**
     * 动态获取Item的位置信息
     * @param index item位置
     * Dynamically obtain item location information
     * @return item的Rect信息
     */
    private fun getFrame(index: Int): Rect {
        var frame = mAllItemFrames[index]
        if (frame == null) {
            frame = Rect()
            //The original position is accumulated (that is, the accumulated interval distance)
            val offset = (mStartX + intervalDistance * index).toFloat() //原始位置累加（即累计间隔距离）
            frame[Math.round(offset), mStartY, Math.round(offset + mDecoratedChildWidth)] =
                mStartY + mDecoratedChildHeight
        }
        return frame
    }

    /**
     * 变化Item的灰度值
     * Change the gray value of Item
     * @param child 需要设置灰度值的Item   Item that needs to set the gray value
     * @param frame 位置信息
     */
    private fun greyItem(child: View, frame: Rect) {
        val value = computeGreyScale(frame.left - mOffsetAll)
        val cm = ColorMatrix(
            floatArrayOf(
                value,
                0f,
                0f,
                0f,
                120 * (1 - value),
                0f,
                value,
                0f,
                0f,
                120 * (1 - value),
                0f,
                0f,
                value,
                0f,
                120 * (1 - value),
                0f,
                0f,
                0f,
                1f,
                250 * (1 - value)
            )
        )
        //        cm.setSaturation(0.9f);

        // Create a paint object with color matrix
        val greyPaint = Paint()
        greyPaint.colorFilter = ColorMatrixColorFilter(cm)

        // Create a hardware layer with the grey paint
        child.setLayerType(View.LAYER_TYPE_HARDWARE, greyPaint)
        if (value >= 1) {
            // Remove the hardware layer
            child.setLayerType(View.LAYER_TYPE_NONE, null)
        }
    }

    private fun item3D(child: View, frame: Rect) {
        val center = (frame.left + frame.right - 2 * mOffsetAll) / 2f
        var value: Float =
            (center - (mStartX + mDecoratedChildWidth / 2f)) * 1f / (getItemCount() * intervalDistance)
        value = Math.sqrt(Math.abs(value).toDouble()).toFloat()
        val symbol =
            if (center > mStartX + mDecoratedChildWidth / 2f) (-1).toFloat() else 1.toFloat()
        child.rotationY = symbol * 50 * value
    }

    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        when (state) {
            RecyclerView.SCROLL_STATE_IDLE ->                 //滚动停止时
                //When scrolling stops
                fixOffsetWhenFinishScroll()
            RecyclerView.SCROLL_STATE_DRAGGING -> {
            }
            RecyclerView.SCROLL_STATE_SETTLING -> {
            }
        }
    }

    override fun scrollToPosition(position: Int) {
        if (position < 0 || position > getItemCount() - 1) return
        mOffsetAll = calculateOffsetForPosition(position)
        if (mRecycle == null || mState == null) { //如果RecyclerView还没初始化完，先记录下要滚动的位置
            selectedPos = position
        } else {
            layoutItems(
                mRecycle,
                mState,
                if (position > selectedPos) SCROLL_TO_LEFT else SCROLL_TO_RIGHT
            )
            onSelectedCallBack()
        }
    }

   override fun smoothScrollToPosition(
        recyclerView: RecyclerView?,
        state: RecyclerView.State?,
        position: Int
    ) {
        // TODO 循环模式暂不支持平滑滚动
        //Loop mode does not support smooth scrolling
        if (mIsLoop) return
        val finalOffset = calculateOffsetForPosition(position)
        if (mRecycle == null || mState == null) { //如果RecyclerView还没初始化完，先记录下要滚动的位置
            ////If RecyclerView has not been initialized yet, first record the position to scroll
            selectedPos = position
        } else {
            startScroll(mOffsetAll, finalOffset)
        }
    }

    override fun canScrollHorizontally(): Boolean {
        return true
    }



    /**
     * 获取整个布局的水平空间大小
     * Get the horizontal space size of the entire layout
     */
    private val horizontalSpace: Int
        private get() = getWidth() - getPaddingRight() - getPaddingLeft()

    /**
     * 获取整个布局的垂直空间大小
     * Get the vertical space size of the entire layout
     */
    private val verticalSpace: Int
        private get() = getHeight() - getPaddingBottom() - getPaddingTop()

    /**
     * 获取最大偏移量
     * Get the maximum offset
     */
    private val maxOffset: Int
        private get() = (itemCount - 1) * intervalDistance

    /**
     * 计算Item缩放系数
     * Calculate Item zoom factor
     * @param x Item的偏移量
     * @return 缩放系数
     */
    private fun computeScale(x: Int): Float {
        var scale =
            1 - Math.abs(x - mStartX) * 1.0f / Math.abs(mStartX + mDecoratedChildWidth / mIntervalRatio)
        if (scale < 0) scale = 0f
        if (scale > 1) scale = 1f
        return scale
    }

    /**
     * 计算Item的灰度值
     * Calculate the gray value of Item
     * @param x Item的偏移量
     * @return 灰度系数
     */
    private fun computeGreyScale(x: Int): Float {
        val itemMidPos = (x + mDecoratedChildWidth / 2).toFloat() //item中点x坐标
        val itemDx2Mid = Math.abs(itemMidPos - horizontalSpace / 2f) //item中点距离控件中点距离
        var value = 1 - itemDx2Mid * 1.0f / (horizontalSpace / 2)
        if (value < 0.1) value = 0.1f
        if (value > 1) value = 1f
        value = Math.pow(value.toDouble(), .8).toFloat()
        return value
    }

    /**
     * 计算Item半透值
     * Calculate the item translucent value
     * @param x Item的偏移量
     * @return 缩放系数
     */
    private fun computeAlpha(x: Int): Float {
        var alpha =
            1 - Math.abs(x - mStartX) * 1.0f / Math.abs(mStartX + mDecoratedChildWidth / mIntervalRatio)
        if (alpha < 0.3f) alpha = 0.3f
        if (alpha > 1) alpha = 1.0f
        return alpha
    }

    /**
     * 计算Item所在的位置偏移
     * Calculate the position offset of the Item
     * @param position 要计算Item位置
     */
    private fun calculateOffsetForPosition(position: Int): Int {
        return Math.round((intervalDistance * position).toFloat())
    }

    /**
     * 修正停止滚动后，Item滚动到中间位置
     * Fixed the item scrolling to the middle position after stopping scrolling
     */
    private fun fixOffsetWhenFinishScroll() {
        // Judgment is not 0, otherwise dividing by 0 will cause an exception
        if (intervalDistance != 0) { // 判断非 0 ，否则除 0 会导致异常
            var scrollN = (mOffsetAll * 1.0f / intervalDistance).toInt()
            val moreDx = (mOffsetAll % intervalDistance).toFloat()
            if (Math.abs(moreDx) > intervalDistance * 0.5) {
                if (moreDx > 0) scrollN++ else scrollN--
            }
            val finalOffset = scrollN * intervalDistance
            startScroll(mOffsetAll, finalOffset)
            selectedPos =
                Math.abs(Math.round(finalOffset * 1.0f / intervalDistance)) % getItemCount()
        }
    }

    /**
     * 滚动到指定X轴位置
     * Scroll to the specified X axis position
     * @param from X轴方向起始点的偏移量 Offset of the starting point in the X-axis direction
     * @param to X轴方向终点的偏移量 Offset of the end point in the X axis direction
     */
    private fun startScroll(from: Int, to: Int) {
        if (mAnimation != null && mAnimation!!.isRunning) {
            mAnimation!!.cancel()
        }
        val direction = if (from < to) SCROLL_TO_LEFT else SCROLL_TO_RIGHT
        mAnimation = ValueAnimator.ofFloat(from.toFloat(), to.toFloat())
        mAnimation?.setDuration(500)
        mAnimation?.setInterpolator(DecelerateInterpolator())
        mAnimation?.addUpdateListener(AnimatorUpdateListener { animation ->
            mOffsetAll = Math.round(animation.animatedValue as Float)
            layoutItems(mRecycle, mState, direction)
        })
        mAnimation?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                onSelectedCallBack()
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
        mAnimation?.start()
    }

    /**
     * 获取Item间隔
     * Get item interval
     */
    private val intervalDistance: Int
        private get() = Math.round(mDecoratedChildWidth * mIntervalRatio)

    /**
     * 计算当前选中位置，并回调
     * Calculate the currently selected position and call back
     */
    private fun onSelectedCallBack() {
        selectedPos = Math.round((mOffsetAll / intervalDistance).toFloat())
        selectedPos = Math.abs(selectedPos % getItemCount())
        if (mSelectedListener != null && selectedPos != mLastSelectPosition) {
            mSelectedListener!!.onItemSelected(selectedPos)
        }
        mLastSelectPosition = selectedPos
    }

    private fun checkTag(tag: Any?): TAG? {
        return if (tag != null) {
            if (tag is TAG) {
                tag
            } else {
                throw IllegalArgumentException("You should not use View#setTag(Object tag), use View#setTag(int key, Object tag) instead!")
            }
        } else {
            null
        }
    }

    /**
     * 获取第一个可见的Item位置
     * Get the first visible Item position
     *
     * Note:该Item为绘制在可见区域的第一个Item，有可能被第二个Item遮挡
     *
     * Note: This Item is the first Item drawn in the visible area, and may be blocked by the second Item
     */
    val firstVisiblePosition: Int
        get() {
            val displayFrame = Rect(
                mOffsetAll, 0, mOffsetAll + horizontalSpace,
                verticalSpace
            )
            val cur = centerPosition
            var i = cur - 1
            while (true) {
                val rect = getFrame(i)
                if (rect.left <= displayFrame.left) {
                    return Math.abs(i) % getItemCount()
                }
                i--
            }
        }

    /**
     * 获取最后一个可见的Item位置
     * Get the last visible Item position
     *
     * Note:该Item为绘制在可见区域的最后一个Item，有可能被倒数第二个Item遮挡
     *
     * Note: This Item is the last Item drawn in the visible area, and may be blocked by the penultimate Item
     */
    val lastVisiblePosition: Int
        get() {
            val displayFrame = Rect(
                mOffsetAll, 0, mOffsetAll + horizontalSpace,
                verticalSpace
            )
            val cur = centerPosition
            var i = cur + 1
            while (true) {
                val rect = getFrame(i)
                if (rect.right >= displayFrame.right) {
                    return Math.abs(i) % getItemCount()
                }
                i++
            }
        }

    /**
     * 该方法主要用于[RecyclerCoverFlow.getChildDrawingOrder]判断中间位置
     * This method is mainly used for [RecyclerCoverFlow.getChildDrawingOrder]
     * to determine the intermediate position
     * @param index child 在 RecyclerCoverFlow 中的位置
     * @return child 的实际位置，如果 [.mIsLoop] 为 true ，返回结果可能为负值
     * The actual position of the child, if [.mIsLoop] is true, the returned result may be negative
     */
    fun getChildActualPos(index: Int): Int {
        val child: View? = getChildAt(index)
        return if (child!!.tag != null) {
            val tag = checkTag(child.tag)
            tag!!.pos
        } else {
            getPosition(child)
        }
    }

    /**
     * 获取可见范围内最大的显示Item个数
     * Get the largest number of displayed items in the visible range
     */
    val maxVisibleCount: Int
        get() {
            val oneSide = (horizontalSpace - mStartX) / intervalDistance
            return oneSide * 2 + 1
        }

    /**
     * 获取中间位置
     * Get the middle position
     *
     * Note:该方法主要用于[RecyclerCoverFlow.getChildDrawingOrder]判断中间位置
     *
     * Note: This method is mainly used to [RecyclerCoverFlow.getChildDrawingOrder]
     * to determine the intermediate position
     *
     * 如果需要获取被选中的Item位置，调用[.getSelectedPos]
     *
     * If you need to get the selected Item position, call [.getSelectedPos]
     */
    val centerPosition: Int
        get() {
            Log.e("MY TAG", "OFFSET $mOffsetAll")
            var pos = mOffsetAll / intervalDistance
            val more = mOffsetAll % intervalDistance
            if (Math.abs(more) >= intervalDistance * 0.5f) {
                if (more >= 0) pos++ else pos--
            }
            return pos
        }

    /**
     * 设置选中监听
     * Set selected monitor
     * @param l 监听接口
     * @param l monitoring interface
     */
    fun setOnSelectedListener(l: OnSelected?) {
        mSelectedListener = l
    }

    /**
     * 选中监听接口
     */
    interface OnSelected {
        /**
         * 监听选中回调
         * @param position 显示在中间的Item的位置
         */
        fun onItemSelected(position: Int)
    }

    private inner class TAG internal constructor(var pos: Int)
    internal class Builder {
        var isFlat = false
        var isGreyItem = false
        var isAlphaItem = false
        var cstIntervalRatio = -1f
        var isLoop = false
        var is3DItem = false
        fun setFlat(flat: Boolean): Builder {
            isFlat = flat
            return this
        }

        fun setGreyItem(greyItem: Boolean): Builder {
            isGreyItem = greyItem
            return this
        }

        fun setAlphaItem(alphaItem: Boolean): Builder {
            isAlphaItem = alphaItem
            return this
        }

        fun setIntervalRatio(ratio: Float): Builder {
            cstIntervalRatio = ratio
            return this
        }

        fun loop(): Builder {
            isLoop = true
            return this
        }

        fun set3DItem(d3: Boolean): Builder {
            is3DItem = true
            return this
        }

        fun build(): CoverFlowLayoutManger {
            return CoverFlowLayoutManger(
                isFlat, isGreyItem,
                isAlphaItem, cstIntervalRatio, isLoop, is3DItem
            )
        }
    }

    companion object {
        /**item 向右移动 */
        /**item move right */
        private const val SCROLL_TO_RIGHT = 1
        /**item 向左移动 */
        /**item move left */
        private const val SCROLL_TO_LEFT = 2
        /**
         * 最大存储item信息存储数量，
         * 超过设置数量，则动态计算来获取
         */
        /**
         * The maximum storage quantity of item information,
         * If the set number is exceeded, it will be obtained by dynamic calculation
         */
        private const val MAX_RECT_COUNT = 100
    }

    init {
        mIsFlatFlow = isFlat
        mItemGradualGrey = isGreyItem
        mItemGradualAlpha = isAlphaItem
        mIsLoop = isLoop
        mItem3D = is3DItem
        if (cstInterval >= 0) {
            mIntervalRatio = cstInterval
        } else {
            if (mIsFlatFlow) {
                mIntervalRatio = 1.1f
            }
        }
    }

    override fun isAutoMeasureEnabled(): Boolean {
        return true
    }
}