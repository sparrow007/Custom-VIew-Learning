package com.example.customviewimple.layoutManager;

import android.graphics.Rect;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;

public class CoverFlowLayoutManger extends RecyclerView.LayoutManager {

    private SparseArray<Rect> mAllItemFrames = new SparseArray<>();
    private SparseArray<Rect> mHasAttachedItems = new SparseArray<>();

    private int decoratedWidth , decoratedHeight;

    private float startX, startY;


    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);

        if(state.isPreLayout() || state.getItemCount() == 0)
            return;


        //Clear the all item frames which is using to stores the locations of item
        mAllItemFrames.clear();

        //HasAttachedItems used to store the child for the control
        mHasAttachedItems.clear();

        View scrap = recycler.getViewForPosition(0);
        addView(scrap);
        measureChildWithMargins(scrap, 0, 0);

        decoratedWidth = getDecoratedMeasuredWidth(scrap);
        decoratedHeight = getDecoratedMeasuredHeight(scrap);

        //Used to show the center for the view because first child used to show at center

        startX = Math.round(getHorizontalSpace() - decoratedWidth) * 1.0f/ 2;
        startY = Math.round(getVerticalSpace()  - decoratedHeight) * 1.0f / 2;



    }

    private int getHorizontalSpace() {
        return getWidth() - getPaddingRight() - getPaddingLeft();
    }

    private int getVerticalSpace() {
        return getHeight() - getPaddingBottom() - getPaddingTop();
    }
}