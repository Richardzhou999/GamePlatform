package com.unis.gameplatfrom.ui.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

import com.unis.gameplatfrom.utils.LogUtil;

public class GameLinearLayoutManager extends LinearLayoutManager {
    public GameLinearLayoutManager(Context context) {
        super(context);
    }

    public GameLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public GameLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            Log.e("problem", "meet a IOOBE in RecyclerView");
        }
    }

    @Override
    public void scrollToPosition(int position) {
        try {
            super.scrollToPosition(position);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d("catch exception");
        }
    }

}
