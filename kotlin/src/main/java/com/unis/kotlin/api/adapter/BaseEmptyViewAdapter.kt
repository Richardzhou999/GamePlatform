package com.unis.kotlin.api.adapter

import android.content.Context
import android.widget.LinearLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.unis.kotlin.ui.view.ListEmptyView
import java.util.ArrayList

abstract class BaseEmptyViewAdapter<T> : BaseQuickAdapter<T,BaseViewHolder>{

    constructor(context: Context,layoutResId:Int,list:List<T>):this(context,
            true,layoutResId,list)

    constructor(ctx: Context, showEmpty: Boolean, layoutResId: Int, list: List<T>?) : super(layoutResId,list ?:ArrayList()) {
        if (showEmpty) {
            setEmptyMessage(ctx, "暂无数据")
        }
    }

    fun setEmptyMessage(ctx: Context, message: String) {
        val emptyView = ListEmptyView(ctx)
        emptyView.setMessage(message)
        emptyView.setLayoutParams(LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT))
        setEmptyView(emptyView)
    }

}