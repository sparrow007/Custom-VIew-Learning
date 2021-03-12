package com.example.customviewimple.layoutManager.source

import android.content.Context
import android.util.AttributeSet
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

    private var customLayoutManagerBuilder: CoverLayout.Builder = CoverLayout.Builder()

    init {
        layoutManager = customLayoutManagerBuilder.build()
        isChildrenDrawingOrderEnabled = true
    }

    fun set3DItem() {
        customLayoutManagerBuilder.set3DItem(true)
        layoutManager = customLayoutManagerBuilder.build()
    }

    fun setInfinite() {
        customLayoutManagerBuilder.setIsInfinite(true)
        layoutManager = customLayoutManagerBuilder.build()
    }

    fun getCoverLayoutManager(): CoverFlowLayoutManger {
        return layoutManager as CoverFlowLayoutManger
    }

     fun getCoverLayout(): CoverLayout {
        return layoutManager as CoverLayout
    }

    override fun getChildDrawingOrder(childCount: Int, i: Int): Int {
        val center: Int = getCoverLayout().centerPosition()

        // Get the actual position of the i-th child view in RecyclerView

        // Get the actual position of the i-th child view in RecyclerView
        val actualPos: Int = getCoverLayout().getChildActualPos(i)

        // The number of intervals from the middle item

        // The number of intervals from the middle item
        val dist = actualPos - center

        var order: Int
        // [< 0] indicates that the item is located to the left of the middle item and can be drawn in order
        // [< 0] indicates that the item is located to the left of the middle item and can be drawn in order
        order = if (dist < 0) {
            i
        } else {
            //[>= 0] It means that the item is located to the right
            // of the middle item, and the order needs to be reversed.
            childCount - 1 - dist
        }

        if (order < 0) order = 0 else if (order > childCount - 1) order = childCount - 1

        return order
    }

    fun setItemSelectListener(listener: CoverLayout.OnSelected) {
        getCoverLayout().setOnSelectedListener(listener)
    }

    /**
     * Get selected position from the layout manager
     * @return center view of the layout manager
     */
    fun getSelectedPosition() = getCoverLayout().getSelectedPosition()

}