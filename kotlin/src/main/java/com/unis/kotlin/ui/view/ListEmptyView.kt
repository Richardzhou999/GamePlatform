package com.unis.kotlin.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.unis.kotlin.R
import kotlinx.android.synthetic.main.list_empty_view.view.*

class ListEmptyView :RelativeLayout{

    constructor(context: Context?) : super(context){
        initWidthContext(context)
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        initWidthContext(context)
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        initWidthContext(context)
    }


    private fun initWidthContext(ctx: Context?) {
        LayoutInflater.from(ctx).inflate(R.layout.list_empty_view, this, true)


    }

    fun setMessage(msg: String) {
        tv_enpty_view_msg.setText(msg)
    }




}