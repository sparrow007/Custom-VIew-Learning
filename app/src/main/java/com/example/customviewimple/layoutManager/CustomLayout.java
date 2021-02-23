package com.example.customviewimple.layoutManager;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;


public class CustomLayout extends RecyclerView.LayoutManager {

    private final String TAG = "LAYOUT_MANAGER";
    private int mVisibleOffset;
    private int mFirstVisiPos;
    private int mLastVisiPos;
    private int mPendingScrollPosition = -1;

    private int mItemWidth;
    private int mItemHeight;

    private final float mChildScale = 0.5f;

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
           super.onLayoutChildren(recycler, state);
        if (state.getItemCount() == 0) {
            removeAndRecycleAllViews(recycler);
        } else {
            if ((state.isPreLayout() || !state.didStructureChange()) && mPendingScrollPosition == -1) {
                return;
            }
        }
        detachAndScrapAttachedViews(recycler);

        View view = recycler.getViewForPosition(0);
        measureChildWithMargins(view, 0, 0);
        mItemWidth  =  getDecoratedMeasureHorizontal(view);
        mItemHeight =  getDecoratedMeasureVertical(view);
        Log.e("MY TAG", "GET DECORATE VERTICAL " + getDecoratedMeasureVertical(view));

        removeAndRecycleView(view, recycler);

        mVisibleOffset = 0;
        mFirstVisiPos = 0;
        mLastVisiPos = getItemCount();
        Log.e("MY TAG", "LAST VIEW POST " + getItemCount() + " CHILD View " + getChildCount());
        fill(recycler);
    }

    private void fill(RecyclerView.Recycler recycler) {
        int count = getVisibleCount();

        int topOffset = getPaddingTop();
        int leftOffset = getPaddingLeft();
        int lineMaxLength = 0;
        int minPos = mFirstVisiPos;
        mLastVisiPos = getItemCount() - 1;


        for (int i = minPos; i < count; i++) {
            View childView = recycler.getViewForPosition(0);
            Log.e("MY TAG" , "I AM IN child " + childView.getWidth());
            addView(childView);
            measureChildWithMargins(childView, 0, 0);
            int decoratedWidth = getDecoratedMeasureHorizontal(childView);
            int decorateHeight = getDecoratedMeasureVertical(childView);
            Log.e("MY TAG" , "I AM IN WIDTH " + decoratedWidth);
            Log.e("MY TAG" , "I AM IN HEIGHT  " + decorateHeight);
            layoutDecorated(childView, leftOffset, getPaddingTop(), leftOffset+ decoratedWidth, getPaddingTop()+decorateHeight);
            if(i > minPos)
            scaleChildView(childView);
            leftOffset += decoratedWidth;
            Log.e("MY TAG", "VISIBLE left off  = "+leftOffset);


        }



//        for (int i  = minPos; i <= mLastVisiPos; i++) {
//            View childView = recycler.getViewForPosition(i);
//            addView(childView);
//            measureChildWithMargins(childView, 0, 0);
//            int decoratedWidth = getDecoratedMeasureHorizontal(childView);
//            int decorateHeight = getDecoratedMeasureVertical(childView);
//            if(leftOffset + decoratedWidth <= getHorizontalSpace()) {
//                layoutDecoratedWithMargins(childView, leftOffset, topOffset, leftOffset + decoratedWidth, topOffset + decorateHeight);
//               leftOffset += decoratedWidth;
//               lineMaxLength = Math.max(lineMaxLength, decorateHeight);
//            }else {
//                leftOffset = getPaddingLeft();
//                topOffset += lineMaxLength;
//                lineMaxLength = 0;
//                layoutDecoratedWithMargins(childView, leftOffset, topOffset,leftOffset + decoratedWidth, topOffset + decorateHeight);
//                leftOffset += decoratedWidth;
//                lineMaxLength = Math.max(lineMaxLength, decorateHeight);
//
//            }
//        }

    }


    //get the translation y values in -y axis
    private float getYTranslate(float mScale) {
        Log.e("MY TAG", "VALUES DIV " + mItemHeight/2);
        float value = mItemHeight / 2;
        Log.e("MY TAG", "MULTIPLE = " + (1-mScale));
        return -(mItemHeight/2f)*(1 - mScale) * 1.0f;
    }

    private int getVisibleCount() {
        int mSpace = 0;
        float f = getHorizontalSpace() - mItemWidth - mSpace;
         return (int) Math.ceil((f < 0 ? 0 : f) / (mChildScale * mItemWidth + mSpace) + 1);
    }

    private int getHorizontalSpace() {
     return  getWidth() - getPaddingLeft() - getPaddingRight();
    }

    private int getVerticalSpace() {
        return getHeight() - getPaddingTop() - getPaddingBottom();
    }

   private int getDecoratedMeasureHorizontal(View child) {
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
        Log.e("MY TAG", "LEFT MARGIN " + layoutParams.leftMargin);
        Log.e("MY TAG", "RIGHT MARGIN " + layoutParams.rightMargin);
        return getDecoratedMeasuredWidth(child) + layoutParams.leftMargin + layoutParams.rightMargin;
   }

    private int getDecoratedMeasureVertical(View child) {
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
        Log.e("MY TAG", "TOP MARGIN " + layoutParams.topMargin);
        Log.e("MY TAG", "BOTTOM MARGIN " + layoutParams.bottomMargin);
        return getDecoratedMeasuredHeight(child) + layoutParams.topMargin + layoutParams.bottomMargin;
    }

    @Override
    public boolean canScrollHorizontally() {
        return true;
    }

    @Override
    public boolean isAutoMeasureEnabled() {
        return true;
    }

    private void scaleChildView(View mChildView) {
//        mChildView.setScaleX(mChildScale);
//        mChildView.setScaleY(mChildScale);

        Log.e("MY TAG", "TRANSLATE = " + getYTranslate(mChildScale));
        Log.e("MY TAG", "HEIGHT = " + mItemHeight);


//        mChildView.setTranslationX(-120);
//        mChildView.setTranslationY(getYTranslate(mChildScale));
    }
}
