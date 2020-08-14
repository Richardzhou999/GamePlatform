package com.unis.kotlin.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trello.rxlifecycle2.components.support.RxFragment

abstract class BaseFragment : RxFragment(){

    protected var mContent : Activity? = null

    @LayoutRes
    protected abstract fun getLayout(): Int

    protected abstract fun initView(var1: View, var2: Bundle?)

    protected abstract fun initData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(this.getLayout(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(view,savedInstanceState)
        initData()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

    }


}