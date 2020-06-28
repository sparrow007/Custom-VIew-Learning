package com.example.customviewimple.layoutManager;

import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class CustomLayout extends RecyclerView.LayoutManager {

    private final String TAG = "LAYOUT_MANAGER";
    private int mVisibleOffset;
    private int mFirstVisiPos;
    private int mLastVisiPos;

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {

        if(getItemCount() == 0) {
            detachAndScrapAttachedViews(recycler);
            return;
        }

        if(getChildCount() == 0 && state.isPreLayout())
            return;



        detachAndScrapAttachedViews(recycler);
        mVisibleOffset = 0;
        mFirstVisiPos = 0;
        mLastVisiPos = getItemCount();
        fill(recycler, state);
    }

    private void fill(RecyclerView.Recycler recycler, RecyclerView.State state) {
        int topOffset = getPaddingTop();
        int leftOffset = getPaddingLeft();
        int lineMaxLength = 0;
        int minPos = mFirstVisiPos;
        mLastVisiPos = getItemCount() - 1;

        for (int i  = minPos; i <= mLastVisiPos; i++) {
            View childView = recycler.getViewForPosition(i);
            addView(childView);
            measureChildWithMargins(childView, 0, 0);
            int decoratedWidth = getDecoratedMeasureHorizontal(childView);
            int decorateHeight = getDecoratedMeasureVertical(childView);
            if(leftOffset + decoratedWidth <= getHorizontalSpace()) {
                layoutDecoratedWithMargins(childView, leftOffset, topOffset, leftOffset + decoratedWidth, topOffset + decorateHeight);
               leftOffset += decoratedWidth;
               lineMaxLength = Math.max(lineMaxLength, decorateHeight);
            }else {
                leftOffset = getPaddingLeft();
                topOffset += lineMaxLength;
                lineMaxLength = 0;
                layoutDecoratedWithMargins(childView, leftOffset, topOffset,leftOffset + decoratedWidth, topOffset + decorateHeight);
                leftOffset += decoratedWidth;
                lineMaxLength = Math.max(lineMaxLength, decorateHeight);

            }
        }
    }

    private int getHorizontalSpace() {
     return  getWidth() - getPaddingLeft() - getPaddingRight();
    }

    private int getVerticalSpace() {
        return getHeight() - getPaddingTop() - getPaddingBottom();
    }

   private int getDecoratedMeasureHorizontal(View child) {
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
        return getDecoratedMeasuredWidth(child) + layoutParams.leftMargin + layoutParams.rightMargin;
   }

    private int getDecoratedMeasureVertical(View child) {
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
        return getDecoratedMeasuredWidth(child) + layoutParams.topMargin + layoutParams.bottomMargin;
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

}
