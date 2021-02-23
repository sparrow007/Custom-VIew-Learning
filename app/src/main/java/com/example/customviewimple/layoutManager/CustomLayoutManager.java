package com.example.customviewimple.layoutManager;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

public class CustomLayoutManager extends RecyclerView.LayoutManager {

    private int mVerticalOffset;
    private int mFirstVisPos;
    private int mLastVisPos;

    /*
    * Generate the default layout params for item view type which will generate at the time of
    * getViewForPosition method call and when there is no data attacched to view then
    * new view holder with layout params from below code is used for getting the
    * */
    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {

        if(getItemCount() == 0) {
            detachAndScrapAttachedViews(recycler);
            return;
        }

        if(getChildCount() == 0 && state.isPreLayout()) return;

        detachAndScrapAttachedViews(recycler);

        mVerticalOffset = 0;
        mFirstVisPos = 0;
        mLastVisPos = getItemCount();

        fill(recycler, state);
    }

    private void fill(RecyclerView.Recycler recycler, RecyclerView.State state) {
        int topOffset = getPaddingTop();
        int leftOffset = getPaddingLeft();
        int lineMaxOffset = 0;
        int minPos = mFirstVisPos;
        mLastVisPos = getItemCount() - 1;;

    }

    private int childHorizontalSpace(View childView) {
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)  childView.getLayoutParams();
        return childView.getWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
    }

    private int childVerticalSpace(View childView) {
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)  childView.getLayoutParams();
        return childView.getHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
    }

    private int verticalSpace() {
        return getHeight() - getPaddingTop() - getPaddingBottom();
    }

    private int horizontalSpace() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }


}