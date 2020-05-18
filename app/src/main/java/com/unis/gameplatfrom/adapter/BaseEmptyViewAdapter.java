package com.unis.gameplatfrom.adapter;

import android.content.Context;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;


import java.util.ArrayList;
import java.util.List;

import com.unis.gameplatfrom.ui.view.ListEmptyView;

/**
 * Created by wulei on 2016-08-16.
 */

@SuppressWarnings("DefaultFileTemplate")
public abstract class BaseEmptyViewAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {

    private boolean showEmpty;

    public BaseEmptyViewAdapter(Context ctx, int layoutResId, List<T> list) {
        this(ctx, true, layoutResId, list);
    }

    public BaseEmptyViewAdapter(Context ctx, boolean showEmpty, int layoutResId, List<T> list) {
        super(layoutResId, list == null ? new ArrayList<T>() : list);
        if (showEmpty) {
            setEmptyMessage(ctx, "暂无数据");
        }
    }

    public void setEmptyMessage(Context ctx, String message) {
        ListEmptyView emptyView = new ListEmptyView(ctx);
        emptyView.setMessage(message);
        emptyView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        setEmptyView(emptyView);
    }

}
