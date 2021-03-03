package com.example.customviewimple.layoutManager.source

import android.graphics.Rect
import android.util.SparseArray
import androidx.recyclerview.widget.RecyclerView

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

    private val mAttachedItems = SparseArray<Rect>()

    private val mHasAttachedItems = SparseArray<Boolean>()


    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT)
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        super.onLayoutChildren(recycler, state)

    }


}