package com.example.customviewimple.layoutManager

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.floor
import kotlin.properties.Delegates

class TechLayoutManager: RecyclerView.LayoutManager() {

    private var viewWidth = 0
    private var viewHeight = 0
    private var mHorizontalScroll = 0


    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.WRAP_CONTENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {

        if (recycler == null || state == null) return

        if (state.isPreLayout || !state.didStructureChange()) return

        if (state.itemCount == 0) return

        val view = recycler.getViewForPosition(0)
        addView(view)
        measureChild(view, 0, 0)

        viewWidth = getDecoratedMeasuredWidth(view)
        viewHeight = getDecoratedMeasuredHeight(view)

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

        mHorizontalScroll += dx
        fill(recycler!!, dx)
        return dx
    }

    private fun fill(recycler: RecyclerView.Recycler, dx: Int) {

        detachAndScrapAttachedViews(recycler)

        Log.e("MY TAG", "Horizontal offset " + mHorizontalScroll)

        val firstVisiblePosition = (floor(mHorizontalScroll.toDouble() / viewWidth.toDouble())).toInt() - 1
        Log.e("MY TAG", "FIRST " + firstVisiblePosition)
        val lastPosition = (mHorizontalScroll + getHorizontalSpace()) / viewWidth
        Log.e("MY TAG", "LAST " + lastPosition)


        var valueIndex = 0
        for (index in firstVisiblePosition..lastPosition) {

            var recyclerIndex = index % itemCount
            if (recyclerIndex < 0) recyclerIndex += itemCount

            Log.e("MY TAG", "I AM INDEX " + recyclerIndex)

            val view = recycler.getViewForPosition(recyclerIndex)
            addView(view)
            measureChildWithMargins(view, 0, 0)

            val l = (index+1)*viewWidth - mHorizontalScroll
            val r = l + viewWidth
            val t = 0
            val b = t + viewHeight
            layoutDecorated(view, l, t, r, b)

            valueIndex++
        }

        val scrapListCopy = recycler.scrapList.toList()
        scrapListCopy.forEach {
            recycler.recycleView(it.itemView)
        }

    }

    private fun getHorizontalSpace(): Int {
        return width - paddingLeft - paddingRight
    }


    override fun isAutoMeasureEnabled(): Boolean {
        return true
    }
}