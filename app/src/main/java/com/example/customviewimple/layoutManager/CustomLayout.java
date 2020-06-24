package com.example.customviewimple.layoutManager;

import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

public class CustomLayout extends RecyclerView.LayoutManager {

    private final String TAG = "LAOUTMANAGER";

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        Log.e(TAG, "You have reach to onlayout children " + recycler + " and "+ state);

    }


}
